package com.team3.assign_back.domain.team.repository;

import com.team3.assign_back.domain.team.dto.TeamResponseDto;
import com.team3.assign_back.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT new com.team3.assign_back.domain.team.dto.TeamResponseDto(t.id, t.name) FROM Team t")
    List<TeamResponseDto> findAllTeams();

    Optional<Team> findByName(String name);
}