package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySummaryMonthlyDto {
    private int year;
    private int month;
    private int day;
    private CompanySummaryMonthly.Statistics statistics;

    public static CompanySummaryMonthlyDto fromEntity(CompanySummaryMonthly entity) {
        return new CompanySummaryMonthlyDto(
                entity.getYear(),
                entity.getMonth(),
                entity.getDay(),
                entity.getStatistics()
        );
    }
}
