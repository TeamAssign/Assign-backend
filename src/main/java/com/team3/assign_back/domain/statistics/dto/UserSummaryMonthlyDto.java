package com.team3.assign_back.domain.statistics.dto;

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
public class UserSummaryMonthlyDto {
    private Long userId;
    private int year;
    private int month;
    private int day;
    private StatisticsDto statistics;

    public static UserSummaryMonthlyDto fromEntity(UserSummaryMonthly entity) {
        return new UserSummaryMonthlyDto(
                entity.getUserId(),
                entity.getYear(),
                entity.getMonth(),
                entity.getDay(),
                StatisticsDto.from(entity.getStatistics())
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatisticsDto {
        private int totalCount;
        private Map<String, Integer> categories;

        public static StatisticsDto from(UserSummaryMonthly.Statistics statistics) {
            return new StatisticsDto(
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

