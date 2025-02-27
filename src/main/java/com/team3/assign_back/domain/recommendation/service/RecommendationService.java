package com.team3.assign_back.domain.recommendation.service;


import com.team3.assign_back.domain.recommendation.dto.KakaoPlaceResponse;
import com.team3.assign_back.domain.recommendation.dto.PlaceResponseDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationRequestDto;
import com.team3.assign_back.domain.recommendation.repository.CustomRecommendationRepository;
import com.team3.assign_back.domain.recommendation.util.KakaoApiService;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.team3.assign_back.global.exception.ErrorCode.EMPTY_PARTICIPANTS;
import static com.team3.assign_back.global.exception.ErrorCode.RECOMMENDATION_EXHAUSTED;


@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final CustomRecommendationRepository customRecommendationRepository;
    private final KakaoApiService kakaoApiService;
    private final ExecutorService executorService;
    private final RedisTemplate redisTemplate;


    public RecommendationResponseDto getRecommendation(Long userId, RecommendationRequestDto recommendationRequestDto){

        //Redis 사용 후 로직 추가할 계획
        List<Long> rejectedFoodIds = new ArrayList<>();
        rejectedFoodIds.add(-1L);

        FoodEnum.FoodCategory category = recommendationRequestDto.getCategory();
        List<Long> participants = recommendationRequestDto.getParticipants();
        FoodEnum.FoodType type = recommendationRequestDto.getType();

        try{
            return switch (type) {
                case SOLO -> customRecommendationRepository.getRecommendation(userId, category, rejectedFoodIds);
                case COMPANYDINNER -> customRecommendationRepository.getRecommendationForTeam(userId, category, rejectedFoodIds);
                case GROUP -> {
                    if (participants == null || participants.isEmpty() || (participants.contains(userId) && participants.size() == 1)) {
                        throw new CustomException(EMPTY_PARTICIPANTS);
                    }
                    if (!participants.contains(userId)) {
                        participants.add(userId);
                    }
                    yield customRecommendationRepository.getRecommendation(category, participants, rejectedFoodIds);
                }
            };
        }catch (EmptyResultDataAccessException e) {
            // 결과가 없는 경우 (NoResultException이 변환됨)
            log.warn("recommendation,{},{},{},{},결과없음", userId, category.name(), type.name(), participants);
            throw new CustomException(RECOMMENDATION_EXHAUSTED);

        } catch (IncorrectResultSizeDataAccessException e) {
            // 여러 결과가 있는 경우 (NonUniqueResultException이 변환됨)
            log.warn("recommendation,{},{},{},{},다중결과발생", userId, category.name(), type.name(), participants);
            throw e;

        } catch (DataAccessException e) {
            // 기타 데이터 접근 예외
            log.warn("recommendation,{},{},{},{},데이터접근오류", userId, category.name(), type.name(), participants, e);
            throw e;

        } catch (ClassCastException | NullPointerException | NumberFormatException e) {
            // 데이터 변환 관련 예외 (Spring이 자동 변환하지 않는 예외들)
            log.warn("recommendation,{},{},{},{},데이터변환오류", userId, category.name(), type.name(), participants, e);
            throw e;
        } catch (Exception e){
            log.warn("recommendation,{},{},{},{},모르는오류", userId, category.name(), type.name(), participants, e);
            throw e;
        }






    }

    @Cacheable(value = "longCache", key = "#root.args[0]", cacheManager = "cacheManager",unless = "#result == null")
    public List<PlaceResponseDto> search(String keyword) {
        List<KakaoPlaceResponse.Document> places = kakaoApiService.findPlaces(keyword);

        List<CompletableFuture<PlaceResponseDto>> futures = places.stream()
                .map(place -> CompletableFuture.supplyAsync(() -> {
                    String imageUrl = kakaoApiService.searchImage(place.getPlaceName());
                    return new PlaceResponseDto(place, imageUrl);
                }, executorService))
                .collect(Collectors.toList());
        List<PlaceResponseDto> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return result;
    }




}
