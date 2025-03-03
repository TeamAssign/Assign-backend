package com.team3.assign_back.domain.recommendation.entity;

import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_recommendation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersRecommendation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recommendation_id", nullable = false)
    private Recommendation recommendation;
}
