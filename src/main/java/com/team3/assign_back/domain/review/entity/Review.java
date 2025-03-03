package com.team3.assign_back.domain.review.entity;

import com.team3.assign_back.domain.intermediate.entity.Participant;
import com.team3.assign_back.domain.users.entity.Users;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;


    @OneToOne(mappedBy = "review")
    private DirectReview directReview;

    @OneToOne(mappedBy = "review")
    private RecommendationReview recommendationReview;

    @Builder.Default
    @OneToMany(mappedBy = "review")
    private List<Participant> participants = new ArrayList<>();

}