package com.team3.assign_back.domain.users.entity;

import com.team3.assign_back.domain.intermediate.entity.Participant;
import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Column(nullable = true)
    private String pros; // 호 데이터

    @Column(nullable = true)
    private String cons; // 불 데이터

    @Column(name = "profile_image_url", nullable = false, length = 2000)
    private String profileImgUrl;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Builder.Default
    @OneToMany(mappedBy = "users")
    private Set<UserTastePreference> userTastePreferences = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "users")
    private List<Participant> participants = new ArrayList<>();
}
