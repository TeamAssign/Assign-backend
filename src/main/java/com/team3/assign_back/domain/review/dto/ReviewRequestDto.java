package com.team3.assign_back.domain.review.dto;

import com.team3.assign_back.global.enums.FoodEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long recommendationId;
    private String imgurl;
    private FoodEnum.FoodCategory category;
    private String menu;
    private FoodEnum.FoodType type;
    private String comment;
    private Integer star;
    private List<Long> participants;

}
