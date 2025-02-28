package com.team3.assign_back.domain.review.dto;

import com.team3.assign_back.global.enums.FoodEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private String comment;
    private int star;
    private String imgurl;
    private List<Long> participants;
    private FoodEnum.FoodType type;
    private FoodEnum.FoodCategory category;
    private String food;
    private Long recommendationId;
}
