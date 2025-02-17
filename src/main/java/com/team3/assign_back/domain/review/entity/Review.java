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
    private List<Users_Review> usersReviews = new ArrayList<>();

    @OneToMany
    private List<DirectReview> directReviews = new ArrayList<>();

    @OneToMany
    private List<Recommendation_Review> recommendationReviews = new ArrayList<>();
}
