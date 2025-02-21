package com.team3.assign_back.domain.food.dto;


import com.team3.assign_back.global.enums.FoodEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodAnalysisRequestDto {

    private FoodEnum.FoodCategory category;

    private List<String> foodNames;


}
