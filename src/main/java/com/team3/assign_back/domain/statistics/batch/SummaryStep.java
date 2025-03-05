package com.team3.assign_back.domain.statistics.batch;

import com.team3.assign_back.domain.intermediate.service.TagService;
import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.domain.statistics.service.SummaryService;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class SummaryStep {

    private final SummaryService summaryService;
    private final UserRepository usersRepository;
    private final TagService tagService;

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

    public Tasklet saveUserTagsTasklet() {
        return (contribution, chunkContext) -> {
            List<Users> users = usersRepository.findAll();

            List<CompletableFuture<Void>> futures = users.stream()
                    .map(user -> CompletableFuture.runAsync(() -> {
                        try {
                            tagService.saveUserTag(user);
                        } catch (Exception e) {
                            log.error("Error occurred while saving user tags - User ID: {}, Error: {}", user.getId(), e.getMessage());
                        }
                    }))
                    .toList();

            // 모든 비동기 작업이 완료될 때까지 대기
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            log.info("all of users tag saved asynchronously: {}명", users.size());
            return RepeatStatus.FINISHED;
        };
    }
}
