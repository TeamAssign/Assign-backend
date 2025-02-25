package com.team3.assign_back.domain.statistics.batch;

import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.team3.assign_back.global.exception.ErrorCode;

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

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                throw new CustomException(ErrorCode.BATCH_JOB_STATISTIC_FAILED);
            }

            log.info("배치 실행 완료: {}", execution.getStatus());
        } catch (JobExecutionException e) {
            log.error("배치 실행 중 에러 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.BATCH_JOB_STATISTIC_FAILED);
        }
    }
}
