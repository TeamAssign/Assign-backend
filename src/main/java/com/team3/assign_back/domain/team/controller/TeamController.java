package com.team3.assign_back.domain.team.controller;

import com.team3.assign_back.domain.review.dto.ReviewResponseDto;
import com.team3.assign_back.domain.review.service.ReviewService;
import com.team3.assign_back.domain.tastePreference.dto.TastePreferenceUpdateRequestDTO;
import com.team3.assign_back.domain.team.dto.TeamProfileDTO;
import com.team3.assign_back.domain.team.dto.TeamResponseDto;
import com.team3.assign_back.domain.team.service.TeamService;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.domain.users.repository.UserRepository;
import com.team3.assign_back.domain.users.service.UserService;
import com.team3.assign_back.global.common.ApiResponseDto;
import com.team3.assign_back.global.common.PageResponseDto;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team API", description = "팀 관련 API")
public class TeamController {
    private final UserService userService;
    private final TeamService teamService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

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
    public ResponseEntity<ApiResponseDto<TeamProfileDTO>> getTeamTastePreference(
            @PathVariable("teamId") Long teamId
           ){

        TeamProfileDTO teamProfileDTO = teamService.getTeamTastePreference(teamId);
        return ApiResponseDto.from(HttpStatus.OK,"팀 프로필이 조회 되었습니다.", teamProfileDTO);

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
    public ResponseEntity<ApiResponseDto<String>> updateTeamTastePreference(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("teamId") Long teamId,
            @RequestBody TastePreferenceUpdateRequestDTO updatedPreference) {

        String vendorId = jwt.getSubject();
        Long userId = userService.getUserIdByVendorId(vendorId);

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long userTeamId = user.getTeam().getId();
        if(!teamId.equals(userTeamId)){
            throw new CustomException(ErrorCode.INVALID_TEAM_SELECTION);
        }

        teamService.updateTeamTastePreference(teamId, updatedPreference);
        return ApiResponseDto.from(HttpStatus.OK,"팀 맛 선호도가 성공적으로 조회 되었습니다.", "업데이트 완료");
    }

    @Operation(
            summary = "팀 리뷰 조회",
            description = "특정 팀의 리뷰를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팀 리뷰 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.class))
    )
    @Parameter(name = "page", description = "페이지 번호", example = "1")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @Parameter(name = "teamId", description = "조회할 팀 Id", required = true, example = "1")
    @GetMapping("/{teamId}/reviews")
    public ResponseEntity<ApiResponseDto<PageResponseDto<ReviewResponseDto>>> getReviewByTeam(
           @PathVariable("teamId") Long teamId,
           @RequestParam(name = "page", defaultValue = "1") int page,
           @RequestParam(name = "size", defaultValue = "10") int size) {


       Pageable pageable = PageRequest.of(page-1, size);

       PageResponseDto<ReviewResponseDto> reviewResponses = reviewService.getReviewByTeam(teamId, pageable);

       return ApiResponseDto.from(HttpStatus.OK, "팀 후기 조회 결과입니다.", reviewResponses);
   }
}