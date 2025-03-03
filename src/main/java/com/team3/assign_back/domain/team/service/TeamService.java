package com.team3.assign_back.domain.team.service;

import com.team3.assign_back.domain.intermediate.entity.TeamTastePreference;
import com.team3.assign_back.domain.tastePreference.dto.TastePreferenceUpdateRequestDTO;
import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceRepository;
import com.team3.assign_back.domain.tastePreference.repository.TeamTastePreferenceRepository;
import com.team3.assign_back.domain.tastePreference.service.TastePreferenceEmbeddingService;
import com.team3.assign_back.domain.team.dto.TeamProfileDTO;
import com.team3.assign_back.domain.team.dto.TeamResponseDto;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.domain.team.repository.TeamRepository;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TastePreferenceRepository tastePreferenceRepository;
    private final TeamTastePreferenceRepository teamTastePreferenceRepository;
    private final TeamRepository teamRepository;
    private final TastePreferenceEmbeddingService tastePreferenceEmbeddingService;

    public List<TeamResponseDto> getAllTeams() {
        return teamRepository.findAllTeams();
    }

    @Transactional(readOnly = true)
    public TeamProfileDTO getTeamTastePreference(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        TeamTastePreference teamTastePreference = teamTastePreferenceRepository.findByTeam(team)
                .orElseThrow(() -> new CustomException(ErrorCode.TASTE_PREFERENCE_NOT_FOUND));

        TastePreference getPreference = teamTastePreference.getTastePreference();
        return new TeamProfileDTO(
                team.getName(),
                getPreference.getSpicy(),
                getPreference.getSweet(),
                getPreference.getSalty(),
                getPreference.getPros(),
                getPreference.getCons()
        );
    }

    @Transactional
    public void updateTeamTastePreference(Long teamId, TastePreferenceUpdateRequestDTO updateRequestDTO){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        TeamTastePreference teamTastePreference = teamTastePreferenceRepository.findByTeam(team)
                .orElseThrow(() -> new CustomException(ErrorCode.TASTE_PREFERENCE_NOT_FOUND));

        TastePreference existingPreference = teamTastePreference.getTastePreference();
        existingPreference.updateTastePreferences(updateRequestDTO);

        tastePreferenceRepository.save(existingPreference);
        tastePreferenceEmbeddingService.saveOrUpdateEmbedding(existingPreference);
    }


}