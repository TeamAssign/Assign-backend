package com.team3.assign_back.domain.food.entity;

import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taste_metrics_embedding")
public class TasteMetricsEmbedding extends BaseEntity {

    @Id
    private Long tasteMetricsId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "taste_metrics_id")
    private TasteMetrics tasteMetrics;

    @Column(name = "text_embedding", columnDefinition = "vector(512)")
    private float[] textEmbedding;

    @Column(name = "metrics_embedding", columnDefinition = "vector(3)")
    private float[] metricsEmbedding;


}