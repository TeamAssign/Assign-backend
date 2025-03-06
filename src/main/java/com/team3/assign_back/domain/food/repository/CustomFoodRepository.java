package com.team3.assign_back.domain.food.repository;


import com.team3.assign_back.domain.food.dto.FoodNotHavingImageRequestDto;
import com.team3.assign_back.domain.food.dto.FoodNotHavingImageResponseDto;

import java.util.List;

public interface CustomFoodRepository{

    List<FoodNotHavingImageRequestDto> customFindFoodsWithoutImage();

    void customSaveFoodImages(List<FoodNotHavingImageResponseDto> foodNotHavingImageResponseDtos);
}
