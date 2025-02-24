package com.team3.assign_back.domain.statistics.controller;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.domain.statistics.service.SummaryService;
import com.team3.assign_back.domain.statistics.batch.BatchScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
@Slf4j
public class SummaryController {
    private final SummaryService summaryService;
    private final BatchScheduler batchScheduler;


    // --- 통계 데이터 조회
    @GetMapping("/user/{userId}")
    public UserSummaryMonthly getUserSummary(@PathVariable(name = "userId") Long userId){
        return summaryService.getLatestUserSummary(userId);
    }

    @GetMapping("/team/{teamId}")
    public TeamSummaryMonthly getTeamSummary(@PathVariable(name = "teamId") Long teamId) {
        return summaryService.getLatestTeamSummary(teamId);
    }

    @GetMapping("/company")
    public CompanySummaryMonthly getCompanySummary(){
        return summaryService.getLatestCompanySummary();
    }



    // ---  수동 배치 실행 API
    @PostMapping("/batch")
    public ResponseEntity<String> runBatchManually() {
        try {
            batchScheduler.executeBatch(); // 기존 메서드 호출
            return ResponseEntity.ok("수동 배치 실행 요청 완료");
        } catch (Exception e) {
            log.error("수동 배치 실행 요청 실패: ", e);
            return ResponseEntity.internalServerError().body("배치 실행 실패: " + e.getMessage());
        }
    }
}
