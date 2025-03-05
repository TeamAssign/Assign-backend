package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
import com.team3.assign_back.global.enums.PriceEnum;
import com.team3.assign_back.global.enums.TagEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecommendationStatsDto {
    private Long userId;
    private int year;
    private int month;
    private PriceEnum.PriceCategory priceCategory;
    private Double agreePercentage;
    private List<TagEnum.MealTag> mealTags;

    public static UserRecommendationStatsDto fromEntity(UserRecommendationStats entity) {
        return UserRecommendationStatsDto.builder()
                .userId(entity.getUserId())
                .year(entity.getYear())
                .month(entity.getMonth())
                .priceCategory(PriceEnum.PriceCategory.fromPrice(entity.getAvgFoodPrice())) // 가격 범주 변환
                .agreePercentage(entity.getAgreePercentage())
                .mealTags(entity.getMealTags())
                .build();

    }

}