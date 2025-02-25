package com.team3.assign_back.domain.food.dto;


import com.team3.assign_back.global.enums.FoodEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponseDto {

    private String name;
    private String imageUrl;
    private BigDecimal accuracy;

}
