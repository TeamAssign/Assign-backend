package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;

import java.util.List;

public interface CustomRecommendationRepository {
    RecommendationResponseDto getRecommendation(FoodEnum.FoodCategory category, List<Long> participants, List<Long> rejectedFoodIds);
    RecommendationResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
    RecommendationResponseDto getRecommendationForTeam(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds);
}
