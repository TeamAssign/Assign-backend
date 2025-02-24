package com.team3.assign_back.domain.team.controller;

import com.team3.assign_back.domain.tastePreference.dto.TastePreferenceUpdateRequestDTO;
import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.team.dto.TeamProfileDTO;
import com.team3.assign_back.domain.team.dto.TeamResponseDto;
import com.team3.assign_back.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;


    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAllTeams() {
        List<TeamResponseDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{temaId}/profile")
    public ResponseEntity<TeamProfileDTO> getTeamTastePreference(
            @PathVariable Long teamId){
        TeamProfileDTO teamProfileDTO = teamService.getTeamTastePreference(teamId);
        return ResponseEntity.ok(teamProfileDTO);
    }


    @PutMapping("/{teamId}/profile")
    public ResponseEntity<String> updateTeamTastePreference(
            @PathVariable Long teamId,
            @RequestBody TastePreferenceUpdateRequestDTO updatedPreference) {

        teamService.updateTeamTastePreference(teamId, updatedPreference);
        return ResponseEntity.ok(("팀 맛 선호도가 성공적으로 업데이트되었습니다."));
    }
}