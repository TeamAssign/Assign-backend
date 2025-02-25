package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.global.enums.FoodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserReviewSummaryDto {
    private Long userId;
    private LocalDateTime createdAt;
    private FoodEnum.FoodCategory category;
    private String foodName;
    private long count;
}
