package com.team3.assign_back.domain.tastePreference.repository;

import com.team3.assign_back.domain.intermediate.entity.TeamTastePreference;
import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamTastePreferenceRepository extends JpaRepository<TeamTastePreference, Long> {
    Optional<TeamTastePreference> findByTeam(Team team);
}