package com.team3.assign_back.domain.food.entity;

import com.team3.assign_back.domain.recommandation.entity.Recommendation;
import com.team3.assign_back.global.common.BaseEntity;
import com.team3.assign_back.global.enums.FoodEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food")
public class Food extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private FoodEnum.FoodCategory category;

    @Column(nullable = false)
    private Integer price;

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

    @Column(nullable = true, length = 2000)
    private String imgUrl;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendations = new ArrayList<>();
}