package com.team3.assign_back.domain.food.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodNotHavingImageRequestDto {
    public Long foodId;
    public String foodName;
    public String category;
}
