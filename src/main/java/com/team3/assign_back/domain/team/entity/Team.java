package com.team3.assign_back.domain.team.entity;

import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.global.common.BaseEntity;
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
@Table(name = "team")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 팀 id

    @Column(nullable = false)
    private String name; // 팀 이름

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer spicy; // 맵기 정도

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer salty; // 짠맛 정도

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer sweet; // 단맛 정도

    @Column(nullable = true, length = 255)
    private String pros; // 호 데이터

    @Column(nullable = true, length = 255)
    private String cons; // 불 데이터

    @OneToMany
    private List<Users> users = new ArrayList<>(); // 팀에 속한 사용자 목록
}
