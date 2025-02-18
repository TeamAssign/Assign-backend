package com.team3.assign_back.domain.food.entity;

import com.team3.assign_back.global.common.BaseEntity;
import com.team3.assign_back.global.enums.FoodEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food_metrics")
public class FoodMetrics extends BaseEntity {

    @Id
    private Long foodId;

    @Column(nullable = false, length = 400)
    private String description;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Float spicy;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Float salty;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Float sweet;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "food_id")
    private Food food;

    @OneToOne(mappedBy = "foodMetrics", fetch = FetchType.LAZY)
    private FoodMetricsEmbedding embedding;

}