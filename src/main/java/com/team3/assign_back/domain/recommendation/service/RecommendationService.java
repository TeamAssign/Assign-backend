package com.team3.assign_back.domain.recommendation.service;


import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.repository.FoodRedisRepository;
import com.team3.assign_back.domain.food.repository.FoodRepository;
import com.team3.assign_back.domain.recommendation.dto.*;
import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import com.team3.assign_back.domain.recommendation.repository.CustomRecommendationRepository;
import com.team3.assign_back.domain.recommendation.repository.RecommendationRepository;
import com.team3.assign_back.domain.recommendation.util.KakaoApiService;
import com.team3.assign_back.domain.tastePreference.service.TastePreferenceEmbeddingService;
import com.team3.assign_back.domain.users.repository.UserRepository;
import com.team3.assign_back.global.common.PageResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.custom.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.team3.assign_back.global.constant.RecommendationConstant.RECOMMENDATION_REJECT_LIST_SIZE_LIMIT;
import static com.team3.assign_back.global.enums.FoodEnum.FoodType.COMPANYDINNER;
import static com.team3.assign_back.global.exception.ErrorCode.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final CustomRecommendationRepository customRecommendationRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final RecommendationRepository recommendationRepository;
    private final FoodRedisRepository foodRedisRepository;

    private final KakaoApiService kakaoApiService;
    private final ExecutorService executorService;
    private final TastePreferenceEmbeddingService tastePreferenceEmbeddingService;


    @Transactional
    public RecommendationResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, FoodEnum.FoodType type, Set<Long> participantIdsSet){

        if(participantIdsSet == null){
            participantIdsSet = new HashSet<>();
        }
        participantIdsSet.add(userId);

        List<Long> participantIds = new ArrayList<>(participantIdsSet);


        List<Long> rejectedFoodIds = getRejectedFoodIds(userId, participantIds, type);
        if(!rejectedFoodIds.isEmpty()){
            rejectRecommendation(userId, type, rejectedFoodIds, participantIds);
        }
        if(rejectedFoodIds.size() > RECOMMENDATION_REJECT_LIST_SIZE_LIMIT){

            deleteRejectedFood(userId, type, participantIds);

            throw new CustomException(RECOMMENDATION_EXHAUSTED);
        }
        rejectedFoodIds.add(-1L);



        List<RecommendationResponseDto> recommendationCandidates;

        try{
            recommendationCandidates = switch (type) {
                case SOLO -> customRecommendationRepository.getRecommendation(userId, category, rejectedFoodIds);
                case COMPANYDINNER -> customRecommendationRepository.getRecommendationForTeam(userId, category, rejectedFoodIds);
                case GROUP -> {
                    if (participantIds.size() == 1) {
                        throw new CustomException(EMPTY_PARTICIPANTS);
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

            deleteRejectedFood(userId, type, participantIds);

            throw new CustomException(RECOMMENDATION_EXHAUSTED);
        }

        RecommendationResponseDto recommendationResponseDto = recommendationCandidates.get(new Random().nextInt(recommendationCandidates.size()));

        saveRejectedFood(userId, type, participantIds, recommendationResponseDto);

        return recommendationResponseDto;
    }

    private void deleteRejectedFood(Long userId, FoodEnum.FoodType type, List<Long> participantIds) {

        String keyPrefix = generateKeyPrefix(userId, participantIds, type);

        foodRedisRepository.deleteRejectedFood(keyPrefix);

    }

    private void saveRejectedFood(Long userId, FoodEnum.FoodType type, List<Long> participantIds, RecommendationResponseDto recommendationResponseDto) {

        Food food = foodRepository.findByName(recommendationResponseDto.getName()).orElseThrow(()-> new CustomException(INVALID_FOOD_NAME));

        String keyPrefix = generateKeyPrefix(userId, participantIds, type);

        foodRedisRepository.saveRejectedFood(keyPrefix, food.getId(), recommendationResponseDto.getAccuracy());

    }

    private List<Long> getRejectedFoodIds(Long userId, List<Long> participantIds, FoodEnum.FoodType type) {

        String keyPrefix = generateKeyPrefix(userId, participantIds, type);

        List<String> rejectedFoodIdsString = foodRedisRepository.getRejectedFoodIds(keyPrefix);
        if(rejectedFoodIdsString == null || rejectedFoodIdsString.isEmpty()){
            return new ArrayList<>();
        }
        return rejectedFoodIdsString.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    private String generateKeyPrefix(Long userId, List<Long> participantIds, FoodEnum.FoodType type) {
        return (type == COMPANYDINNER) ?
                "team:" + userRepository.findTeamIdByUsersId(userId):
                "users:" + participantIds.stream().sorted().map(String::valueOf).collect(Collectors.joining(","));
    }

    public void rejectRecommendation(Long userId, FoodEnum.FoodType type, List<Long> rejectedFoodIds, List<Long> participantIds) {


        int index = rejectedFoodIds.size() - 1;

        Food food = foodRepository.getReferenceById(rejectedFoodIds.get(index));

        String keyPrefix = generateKeyPrefix(userId, participantIds, type);

        BigDecimal accuracy = new BigDecimal(foodRedisRepository.getAccuracy(keyPrefix, index));

        Recommendation recommendation = Recommendation.builder()
                .type(type)
                .isAgree(false)
                .accuracy(accuracy)
                .food(food).build();

        recommendationRepository.save(recommendation);

        Long teamId = null;
        if(type == COMPANYDINNER){
            teamId = userRepository.findTeamIdByUsersId(userId);
        }


        customRecommendationRepository.batchSaveUsersRecommendation(recommendation.getId(), participantIds);
        tastePreferenceEmbeddingService.updateDislikeEmbedding(teamId, participantIds, food.getId());



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

        List<Long> participantIds;
        Long teamId = null;

        if(recommendationRequestDto.getType() == FoodEnum.FoodType.GROUP){
            recommendationRequestDto.getParticipantIds().add(userId);
            participantIds = new ArrayList<>(recommendationRequestDto.getParticipantIds());

        }else{
            participantIds = new ArrayList<>();
            participantIds.add(userId);
        }
        if(recommendationRequestDto.getType() == COMPANYDINNER){
            teamId = userRepository.findTeamIdByUsersId(userId);
        }


        customRecommendationRepository.batchSaveUsersRecommendation(recommendation.getId(), participantIds);
        tastePreferenceEmbeddingService.updateLikeEmbedding(teamId, participantIds, food.getId());

        deleteRejectedFood(userId, recommendationRequestDto.getType(), participantIds);


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


    public PageResponseDto<RecommendationHistoryResponseDto> getRecommendationHistories(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return customRecommendationRepository.getRecommendationHistories(userId, pageable);
    }

    public RecommendationResponseDto getRecommendationToday(Long userId) {


        RecommendationResponseDto recommendationResponseDto = foodRedisRepository.getFoodToday(userId);
        if(recommendationResponseDto != null){
            return recommendationResponseDto;
        }

        List<RecommendationResponseDto> recommendationCandidates = customRecommendationRepository.getRecommendationToday(userId);
        recommendationResponseDto = recommendationCandidates.get(new Random().nextInt(recommendationCandidates.size()));
        foodRedisRepository.saveFoodToday(userId, recommendationResponseDto);

        return recommendationResponseDto;
    }
}
