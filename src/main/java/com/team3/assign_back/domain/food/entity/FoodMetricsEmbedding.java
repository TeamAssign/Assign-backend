package com.team3.assign_back.domain.food.entity;

import com.team3.assign_back.global.common.BaseEntity;
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
@Table(name = "food_metrics_embedding")
public class FoodMetricsEmbedding extends BaseEntity {

    @Id
    private Long foodMetricsId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "food_metrics_id")
    private FoodMetrics foodMetrics;

    @Column(name = "text_embedding", columnDefinition = "vector(512)")
    private float[] textEmbedding;

    @Column(name = "metrics_embedding", columnDefinition = "vector(3)")
    private float[] metricsEmbedding;


}