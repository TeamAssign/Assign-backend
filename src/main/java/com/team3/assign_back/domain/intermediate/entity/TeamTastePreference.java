package com.team3.assign_back.domain.intermediate.entity;

import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team_taste_preference")
public class TeamTastePreference extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taste_preference_id", nullable = false)
    private TastePreference tastePreference;
}