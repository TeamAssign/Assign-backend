package com.team3.assign_back.domain.statistics.entity;

import com.team3.assign_back.global.enums.TagEnum;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "user_recommendation_stats")
@CompoundIndexes({@CompoundIndex(
        name = "user_year_month_idx", def = "{'userId': 1, 'year': -1, 'month': -1}")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecommendationStats {

    @Id
    private String id;

    private Long userId;
    private int year;
    private int month;

    private Double avgFoodPrice;
    private Double agreePercentage;

    private List<TagEnum.MealTag> mealTags;

    public UserRecommendationStats(Long userId, Double avgFoodPrice, Double agreePercentage,  List<TagEnum.MealTag> mealTags) {
        this.userId = userId;
        this.avgFoodPrice = avgFoodPrice;
        this.agreePercentage = agreePercentage;
        this.mealTags = mealTags != null ? mealTags : new ArrayList<>();
    }
}