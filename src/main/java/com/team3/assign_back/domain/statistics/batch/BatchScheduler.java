package com.team3.assign_back.domain.statistics.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job summaryJob;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul") //
    public void runBatchJob(){
        executeBatch();
    }

    public void executeBatch(){
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(summaryJob, jobParameters);
            log.info("배치 실행 완료: {}", execution.getStatus());
        } catch (Exception e) {
            log.error("배치 실행 실패: ", e);
        }
    }
}
