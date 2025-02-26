package com.team3.assign_back.domain.recommendation.dto;


import com.team3.assign_back.global.enums.FoodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationRequestDto {
    private FoodEnum.FoodCategory category;
    private FoodEnum.FoodType type;
    private List<Long> participants;
}
