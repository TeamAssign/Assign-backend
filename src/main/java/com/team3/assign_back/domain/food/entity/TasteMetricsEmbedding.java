package com.team3.assign_back.domain.food.entity;

import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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


    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 256)
    @Column(name = "text_embedding")
    private float[] textEmbedding;


    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 256)
    @Column(name = "metrics_embedding")
    private float[] metricsEmbedding;


}