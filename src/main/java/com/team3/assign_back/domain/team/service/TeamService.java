package com.team3.assign_back.domain.team.service;

import com.team3.assign_back.domain.team.dto.TeamResponseDto;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<TeamResponseDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(team -> new TeamResponseDto(team.getId(), team.getName()))
                .collect(Collectors.toList());
    }
}