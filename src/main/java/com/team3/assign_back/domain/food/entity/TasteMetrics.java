package com.team3.assign_back.domain.food.entity;

import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taste_metrics")
public class TasteMetrics extends BaseEntity {

    @Id
    private Long foodId;

    @Column(nullable = false, length = 400)
    private String description;

    @Column(nullable = false)
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal spicy;

    @Column(nullable = false)
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal salty;

    @Column(nullable = false)
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal sweet;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "food_id")
    private Food food;

}