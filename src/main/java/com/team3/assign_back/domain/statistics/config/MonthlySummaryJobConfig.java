package com.team3.assign_back.domain.statistics.config;

import com.team3.assign_back.domain.statistics.batch.SummaryStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MonthlySummaryJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SummaryStep summaryStep;

    @Bean(name = "monthlySummaryJob")
    public Job monthlySummaryJob() {
        return new JobBuilder("monthlySummaryJob", jobRepository)
                .start(userRecommendationStatsStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public Step userRecommendationStatsStep() {
        return new StepBuilder("userRecommendationStatsStep", jobRepository)
                .tasklet(summaryStep.userRecommendationStatsTasklet(), transactionManager)
                .build();
    }
}