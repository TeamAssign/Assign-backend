package com.team3.assign_back.domain.recommendation.entity;

import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.global.common.BaseEntity;
import com.team3.assign_back.global.enums.FoodEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "recommendation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodEnum.FoodType type;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isAgree;

    @Column(nullable = false)
    private BigDecimal accuracy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;


}
