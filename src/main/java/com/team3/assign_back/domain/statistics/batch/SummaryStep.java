package com.team3.assign_back.domain.statistics.batch;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.domain.statistics.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SummaryStep {

    private final SummaryService summaryService;

    public Tasklet userSummaryTasklet() {
        return (contribution, chunkContext) -> {
            log.info("User Summary Batch 실행 중...");
            List<UserSummaryMonthly> results = summaryService.saveAllUserSummaries();
            log.info("저장된 유저 통계 개수: {}", results.size());
            return RepeatStatus.FINISHED;
        };
    }

    public Tasklet teamSummaryTasklet() {
        return (contribution, chunkContext) -> {
            log.info("Team Summary Batch 실행 중...");
            List<TeamSummaryMonthly> results = summaryService.saveAllTeamSummaries();
            log.info("저장된 팀 통계 개수: {}", results.size());
            return RepeatStatus.FINISHED;
        };
    }

    public Tasklet companySummaryTasklet() {
        return (contribution, chunkContext) -> {
            log.info("Company Summary Batch 실행 중...");
            CompanySummaryMonthly results = summaryService.saveCompanySummary();
            log.info("저장된 회사 통계 데이터 {}", results);
            return RepeatStatus.FINISHED;
        };
    }
}
