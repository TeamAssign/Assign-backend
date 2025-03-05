package com.team3.assign_back.domain.statistics.controller;

import com.team3.assign_back.domain.statistics.dto.CompanySummaryMonthlyDto;
import com.team3.assign_back.domain.statistics.dto.TeamSummaryMonthlyDto;
import com.team3.assign_back.domain.statistics.dto.UserSummaryMonthlyDto;
import com.team3.assign_back.domain.statistics.service.SummaryService;
import com.team3.assign_back.domain.statistics.batch.BatchScheduler;
import com.team3.assign_back.domain.users.service.UserService;
import com.team3.assign_back.global.common.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
@Slf4j
public class SummaryController {
    private final SummaryService summaryService;
    private final BatchScheduler batchScheduler;
    private final UserService userService;


    // --- 통계 데이터 조회
    @GetMapping("/users")
    public ResponseEntity<ApiResponseDto<UserSummaryMonthlyDto>> getUserSummary(@AuthenticationPrincipal Jwt jwt){
        Long userId = userService.getUserIdByVendorId(jwt.getSubject());
        UserSummaryMonthlyDto userSummaryMonthlyDto = summaryService.getLatestUserSummary(userId);
        return ApiResponseDto.from(HttpStatus.OK,"유저 통계 조회 성공",userSummaryMonthlyDto);
    }

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponseDto<TeamSummaryMonthlyDto>> getTeamSummary(@PathVariable(name = "teamId") Long teamId) {
        TeamSummaryMonthlyDto teamSummaryMonthlyDto = summaryService.getLatestTeamSummary(teamId);
        return ApiResponseDto.from(HttpStatus.OK,"팀 통계 조회 성공",teamSummaryMonthlyDto);
    }

    @GetMapping("/company")
    public ResponseEntity<ApiResponseDto<CompanySummaryMonthlyDto>> getCompanySummary(){
        CompanySummaryMonthlyDto companySummaryMonthlyDto = summaryService.getLatestCompanySummary();
        return ApiResponseDto.from(HttpStatus.OK,"전사 통계 조회 성공",companySummaryMonthlyDto);
    }



    // ---  수동 배치 실행 API
    @PostMapping("/batch")
    public ResponseEntity<ApiResponseDto<Void>> runBatchManually() {
        batchScheduler.executeBatch();
        return ApiResponseDto.from(HttpStatus.OK, "수동 배치 실행 요청 완료", null);
    }

    @PostMapping("/preference/batch")
    public ResponseEntity<ApiResponseDto<String>> runBatchUserPreference(){
        summaryService.saveBatchUserPreferenceStats();
        return ApiResponseDto.from(HttpStatus.OK,"사용자별 선호 분석 배치 작업 완료.", null);
    }

}
