package com.team3.assign_back.domain.statistics.batch;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
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
            List<UserSummaryMonthly> results = summaryService.saveAllUserSummaries();
            log.info("User summary records saved: {}", results.size());
            return RepeatStatus.FINISHED;
        };
    }

    public Tasklet teamSummaryTasklet() {
        return (contribution, chunkContext) -> {
            List<TeamSummaryMonthly> results = summaryService.saveAllTeamSummaries();
            log.info("Team summary records saved: {}", results.size());
            return RepeatStatus.FINISHED;
        };
    }

    public Tasklet companySummaryTasklet() {
        return (contribution, chunkContext) -> {
            CompanySummaryMonthly results = summaryService.saveCompanySummary();
            log.info("Company summary data saved: {}", results);
            return RepeatStatus.FINISHED;
        };
    }

    public Tasklet userRecommendationStatsTasklet() {
        return (contribution, chunkContext) -> {
            List<UserRecommendationStats> results = summaryService.saveBatchUserPreferenceStats();
            log.info("User preference summary records saved: {}", results.size());
            return RepeatStatus.FINISHED;
        };
    }
}
