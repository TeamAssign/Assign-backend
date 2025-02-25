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
public class SummaryJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SummaryStep summaryStep;

    @Bean
    public Job summaryJob() {
        return new JobBuilder("summaryJob", jobRepository)
                .start(userSummaryStep())
                .next(teamSummaryStep())
                .next(companySummaryStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public Step userSummaryStep() {
        return new StepBuilder("userSummaryStep", jobRepository)
                .tasklet(summaryStep.userSummaryTasklet(), transactionManager)
                .build();
    }

    public Step teamSummaryStep() {
        return new StepBuilder("teamSummaryStep", jobRepository)
                .tasklet(summaryStep.teamSummaryTasklet(), transactionManager)
                .build();
    }

    public Step companySummaryStep() {
        return new StepBuilder("companySummaryStep", jobRepository)
                .tasklet(summaryStep.companySummaryTasklet(), transactionManager)
                .build();
    }
}
