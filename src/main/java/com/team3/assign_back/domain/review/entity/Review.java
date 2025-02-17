package com.team3.assign_back.domain.review.entity;

import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 후기 id

    @OneToMany
    @Builder.Default
    private List<UsersReview> usersReviews = new ArrayList<>();

    @OneToOne
    private DirectReview directReviews;

    @OneToOne
    private RecommendationReview recommendationReviews;
}
