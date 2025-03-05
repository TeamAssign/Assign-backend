package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.global.enums.FoodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySummaryMonthlyDto {
    private int year;
    private int month;
    private int day;
    private StatisticsDto statistics;

    public static CompanySummaryMonthlyDto fromEntity(CompanySummaryMonthly entity) {
        return new CompanySummaryMonthlyDto(
                entity.getYear(),
                entity.getMonth(),
                entity.getDay(),
                CompanySummaryMonthlyDto.StatisticsDto.from(entity.getStatistics())
        );
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatisticsDto {
        private int totalCount;
        private Map<String, Integer> categories;

        public static CompanySummaryMonthlyDto.StatisticsDto from(CompanySummaryMonthly.Statistics statistics) {
            return new CompanySummaryMonthlyDto.StatisticsDto(
                    statistics.getTotalCount(),
                    statistics.getCategories().entrySet().stream()
                            .collect(Collectors.toMap(
                                    entry -> FoodEnum.FoodCategory.valueOf(entry.getKey()).getKoreanName(),
                                    Map.Entry::getValue
                            ))
            );
        }
    }
}
