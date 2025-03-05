package com.team3.assign_back.domain.statistics.batch;

import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Qualifier;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@Slf4j
@RequiredArgsConstructor
public class MonthlyBatchScheduler {

    private final JobLauncher jobLauncher;
    @Resource(name = "monthlySummaryJob")
    private final Job monthlySummaryJob;

    // 매월 말일 오전 2시에 실행
    @Scheduled(cron = "0 0 2 L * ?", zone = "Asia/Seoul")
    public void runMonthlyBatchJob() {
        executeBatch();
    }

    public void executeBatch() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("month", LocalDate.now(ZoneId.of("Asia/Seoul")).toString()) // 실행된 달 정보 추가
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(monthlySummaryJob, jobParameters);

            if (execution.getStatus() != BatchStatus.COMPLETED) {
                throw new CustomException(ErrorCode.BATCH_JOB_STATISTIC_FAILED);
            }

            log.info("실행 완료: {}", execution.getStatus());
        } catch (JobExecutionException e) {
            log.error("매달 통계 배치 실행 중 에러 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.BATCH_JOB_STATISTIC_FAILED);
        }
    }
}