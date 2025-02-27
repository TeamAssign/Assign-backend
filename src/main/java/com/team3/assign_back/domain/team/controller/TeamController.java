package com.team3.assign_back.domain.team.controller;

import com.team3.assign_back.domain.tastePreference.dto.TastePreferenceUpdateRequestDTO;
import com.team3.assign_back.domain.team.dto.TeamProfileDTO;
import com.team3.assign_back.domain.team.dto.TeamResponseDto;
import com.team3.assign_back.domain.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team API", description = "팀 관련 API")
public class TeamController {
    private final TeamService teamService;

    @Operation(
            summary = "모든 팀 목록 조회",
            description = "현재 존재하는 모든 팀 목록을 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팀 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeamResponseDto.class))
    )
    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAllTeams() {
        List<TeamResponseDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @Operation(
            summary = "팀 프로필 조회",
            description = "특정 팀의 맛 선호도를 포함한 프로필 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팀 프로필 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeamProfileDTO.class))
    )
    @Parameter(name = "teamId", description = "조회할 팀 ID", required = true, example = "1")
    @GetMapping("/{teamId}/profile")
    public ResponseEntity<TeamProfileDTO> getTeamTastePreference(
            @PathVariable Long teamId){
        TeamProfileDTO teamProfileDTO = teamService.getTeamTastePreference(teamId);
        return ResponseEntity.ok(teamProfileDTO);
    }

    @Operation(
            summary = "팀 맛 선호도 업데이트",
            description = "특정 팀의 맛 선호도를 수정합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팀 맛 선호도 업데이트 성공",
            content = @Content(mediaType = "text/plain")
    )
    @Parameter(name = "teamId", description = "업데이트할 팀 ID", required = true, example = "1")
    @PutMapping("/{teamId}/profile")
    public ResponseEntity<String> updateTeamTastePreference(
            @PathVariable Long teamId,
            @RequestBody TastePreferenceUpdateRequestDTO updatedPreference) {

        teamService.updateTeamTastePreference(teamId, updatedPreference);
        return ResponseEntity.ok(("팀 맛 선호도가 성공적으로 업데이트되었습니다."));
    }
}