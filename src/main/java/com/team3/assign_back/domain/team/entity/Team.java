package com.team3.assign_back.domain.team.entity;

import com.team3.assign_back.domain.intermediate.entity.TeamTastePreference;
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

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<Users> users = new ArrayList<>(); // 팀에 속한 사용자 목록

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<TeamTastePreference> teamTastePreferences = new ArrayList<>();
}
