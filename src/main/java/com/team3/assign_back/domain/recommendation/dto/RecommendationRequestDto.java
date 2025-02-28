package com.team3.assign_back.domain.recommendation.dto;


import com.team3.assign_back.global.enums.FoodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationRequestDto {
    private FoodEnum.FoodType type;
    private String name;
    private BigDecimal accuracy;
    private Set<Long> participants;
}
