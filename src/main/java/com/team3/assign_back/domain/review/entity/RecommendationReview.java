package com.team3.assign_back.domain.review.entity;

import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_review")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 추천기반후기 id

    @Column(nullable = false)
    private String comment; // 내용

    @Column(nullable = false)
    private Integer star; // 별점

    @Column(nullable = false, length = 2000)
    private String imgUrl; // 후기 이미지 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

}
