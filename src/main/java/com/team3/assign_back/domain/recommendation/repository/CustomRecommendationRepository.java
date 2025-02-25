package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.food.dto.FoodResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;

import java.util.List;

public interface CustomRecommendationRepository {
    FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, List<Long> participants);
    FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category);
    FoodResponseDto getRecommendationForTeam(Long userId, FoodEnum.FoodCategory category);
}
