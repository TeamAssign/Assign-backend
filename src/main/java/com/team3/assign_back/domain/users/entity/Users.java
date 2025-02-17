package com.team3.assign_back.domain.users.entity;

import com.team3.assign_back.domain.review.entity.UsersReview;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Min(1)
    @Min(5)
    private Integer spicy;

    @Column(nullable = false)
    @Min(1)
    @Min(5)
    private Integer salty;

    @Column(nullable = false)
    @Min(1)
    @Min(5)
    private Integer sweet;

    @Column(nullable = true, length = 255)
    private String pros; // 호 데이터

    @Column(nullable = true, length = 255)
    private String cons; // 불 데이터

    @Column(nullable = false, length = 2000)
    private String profileImgUrl;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany
    private List<UsersReview> usersReviews = new ArrayList<>();
}