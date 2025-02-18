package com.team3.assign_back.domain.review.entity;

import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 후기 id

    @Column(nullable = false)
    private Boolean isReviewer; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users; // 사용자와 연결

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review; // 리뷰와 연결

}
