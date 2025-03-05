package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.recommendation.dto.RecommendationHistoryResponseDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
import com.team3.assign_back.global.common.PageResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CustomRecommendationRepository {
    List<RecommendationResponseDto> getRecommendation(List<Long> participants, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
    List<RecommendationResponseDto> getRecommendation(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
    List<RecommendationResponseDto> getRecommendationForTeam(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);

    void batchSaveUsersRecommendation(Long recommendationId, List<Long> participantIds);

    PageResponseDto<RecommendationHistoryResponseDto> getRecommendationHistories(Long userId, Pageable pageable) throws ExecutionException, InterruptedException;

    List<RecommendationResponseDto> getRecommendationToday(Long userId);

    List<String> getRecommendationForTag(Long userId, int size);

    List<UserRecommendationStats> getUserRecommendationStats();

}
