package com.team3.assign_back.domain.recommendation.service;


import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.repository.FoodRepository;
import com.team3.assign_back.domain.recommendation.dto.KakaoPlaceResponse;
import com.team3.assign_back.domain.recommendation.dto.PlaceResponseDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationRequestDto;
import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import com.team3.assign_back.domain.recommendation.repository.CustomRecommendationRepository;
import com.team3.assign_back.domain.recommendation.repository.RecommendationRepository;
import com.team3.assign_back.domain.recommendation.util.KakaoApiService;
import com.team3.assign_back.domain.tastePreference.service.TastePreferenceEmbeddingService;
import com.team3.assign_back.domain.users.repository.UserRepository;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.custom.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.team3.assign_back.global.exception.ErrorCode.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final CustomRecommendationRepository customRecommendationRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final RecommendationRepository recommendationRepository;



    private final KakaoApiService kakaoApiService;
    private final ExecutorService executorService;
    private final TastePreferenceEmbeddingService tastePreferenceEmbeddingService;


    @Transactional
    public RecommendationResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, FoodEnum.FoodType type, Set<Long> participantIdsSet){

        //Redis 사용 후 로직 추가할 계획
        List<Long> rejectedFoodIds = new ArrayList<>();
        rejectedFoodIds.add(-1L);

        List<Long> participantIds = (participantIdsSet == null) ? new ArrayList<>(): new ArrayList<>(participantIdsSet);

        List<RecommendationResponseDto> recommendationCandidates;

        try{
            recommendationCandidates = switch (type) {
                case SOLO -> customRecommendationRepository.getRecommendation(userId, category, rejectedFoodIds);
                case COMPANYDINNER -> customRecommendationRepository.getRecommendationForTeam(userId, category, rejectedFoodIds);
                case GROUP -> {
                    if (participantIds.isEmpty() || (participantIds.contains(userId) && participantIds.size() == 1)) {
                        throw new CustomException(EMPTY_PARTICIPANTS);
                    }
                    if (!participantIds.contains(userId)) {
                        participantIds.add(userId);
                    }
                    yield customRecommendationRepository.getRecommendation(participantIds, category, rejectedFoodIds);
                }
            };
        }catch (DataAccessException e) {
            // 기타 데이터 접근 예외
            log.warn("recommendation,{},{},{},{},데이터접근오류", userId, category.name(), type.name(), participantIds, e);
            throw e;

        } catch (ClassCastException | NullPointerException | NumberFormatException e) {
            // 데이터 변환 관련 예외 (Spring이 자동 변환하지 않는 예외들)
            log.warn("recommendation,{},{},{},{},데이터변환오류", userId, category.name(), type.name(), participantIds, e);
            throw e;
        } catch (Exception e){
            log.warn("recommendation,{},{},{},{},모르는오류", userId, category.name(), type.name(), participantIds, e);
            throw e;
        }

        if(recommendationCandidates.isEmpty()){
            throw new CustomException(RECOMMENDATION_EXHAUSTED);
        }

        return recommendationCandidates.get(new Random().nextInt(recommendationCandidates.size()));
    }


    private void updateDislike(Long userId, FoodEnum.FoodType type, List<Long> participants, Long foodId){

        if(type == FoodEnum.FoodType.COMPANYDINNER){
            Long teamId = userRepository.findTeamIdByUsersId(userId);
            tastePreferenceEmbeddingService.updateDislikeEmbedding(teamId, null, foodId);
        } else{
            if (!participants.contains(userId)) {
                participants.add(userId);
            }
            tastePreferenceEmbeddingService.updateDislikeEmbedding(null, participants, foodId);

        }

    }

    private void updateLike(Long userId, FoodEnum.FoodType type, List<Long> participants, Long foodId){

        if(type == FoodEnum.FoodType.COMPANYDINNER){
            Long teamId = userRepository.findTeamIdByUsersId(userId);
            tastePreferenceEmbeddingService.updateLikeEmbedding(teamId, null, foodId);
        } else{

            if (participants == null) {
                participants = new ArrayList<>();
            }
            if (!participants.contains(userId)) {
                participants.add(userId);
            }
            tastePreferenceEmbeddingService.updateLikeEmbedding(null, participants, foodId);

        }

    }

    @Transactional
    public void acceptRecommendation(Long userId, RecommendationRequestDto recommendationRequestDto) {

        Food food = foodRepository.findByName(recommendationRequestDto.getName()).orElseThrow(()-> new CustomException(INVALID_FOOD_NAME));

        Recommendation recommendation = Recommendation.builder()
                .type(recommendationRequestDto.getType())
                .isAgree(true)
                .accuracy(recommendationRequestDto.getAccuracy())
                .food(food).build();

        recommendationRepository.save(recommendation);

        List<Long> participants;

        if(recommendationRequestDto.getType() == FoodEnum.FoodType.COMPANYDINNER){
            recommendationRequestDto.getParticipants().add(userId);
            participants = new ArrayList<>(recommendationRequestDto.getParticipants());

        } else{
            participants = new ArrayList<>();
            participants.add(userId);
        }

        customRecommendationRepository.batchSaveUsersRecommendation(recommendation.getId(), participants);




    }


    @Cacheable(value = "longCache", key = "#root.args[0]", cacheManager = "cacheManager",unless = "#result == null")
    public List<PlaceResponseDto> search(String keyword) {
        List<KakaoPlaceResponse.Document> places = kakaoApiService.findPlaces(keyword);

        List<CompletableFuture<PlaceResponseDto>> futures = places.stream()
                .map(place -> CompletableFuture.supplyAsync(() -> {
                    String imageUrl = kakaoApiService.searchImage(place.getPlaceName());
                    return new PlaceResponseDto(place, imageUrl);
                }, executorService))
                .toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }







}
