package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;

import java.util.List;

public interface CustomRecommendationRepository {
    List<RecommendationResponseDto> getRecommendation(List<Long> participants, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
    List<RecommendationResponseDto> getRecommendation(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
    List<RecommendationResponseDto> getRecommendationForTeam(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
}
