package com.team3.assign_back.domain.recommendation.dto;


import com.team3.assign_back.domain.users.dto.UserSearchResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationHistoryResponseDto {

    private Long recommendationId;
    private FoodEnum.FoodType type;
    private FoodEnum.FoodCategory category;
    private String name;
    private BigDecimal accuracy;
    private String imageUrl;
    private boolean isReviewed;
    private List<UserSearchResponseDto> participants;

}
