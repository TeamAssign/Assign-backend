package com.team3.assign_back.domain.food.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.entity.TasteMetrics;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodAnalysisDto {



    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal spicy;

    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal sweet;

    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal salty;



    @JsonAlias("d")
    @Size(min = 100, max = 1000, message = "내용은 100자 이상 1000자 이하여야 합니다")
    private String description;

    @JsonAlias("p")
    @Positive(message = "가격은 양수입니다.")
    private Integer price;


    public TasteMetrics to(Food food){
        return TasteMetrics.builder()
                .food(food)
                .description(description.trim())
                .salty(salty)
                .sweet(sweet)
                .spicy((spicy))
                .build();

    }

    @JsonProperty("taste")
    protected void unpackNested(Map<String, Object> taste){
        this.salty = BigDecimal.valueOf((Double) taste.get("salty"));
        this.sweet = BigDecimal.valueOf((Double) taste.get("sweet"));
        this.spicy = BigDecimal.valueOf((Double) taste.get("spicy"));
    }


}
