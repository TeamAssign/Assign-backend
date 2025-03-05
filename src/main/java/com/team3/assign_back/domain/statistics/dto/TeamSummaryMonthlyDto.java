package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.global.enums.FoodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamSummaryMonthlyDto {
    private Long teamId;
    private int year;
    private int month;
    private int day;
    private StatisticsDto statistics;

    public static TeamSummaryMonthlyDto fromEntity(TeamSummaryMonthly entity) {
        return new TeamSummaryMonthlyDto(
                entity.getTeamId(),
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
        private Map<String, Integer> menu;

        public static StatisticsDto from(TeamSummaryMonthly.Statistics statistics) {
            return new StatisticsDto(
                    statistics.getTotalCount(),
                    statistics.getCategories().entrySet().stream()
                            .collect(Collectors.toMap(
                                    entry -> FoodEnum.FoodCategory.valueOf(entry.getKey()).getKoreanName(),
                                    Map.Entry::getValue
                            )),
                    statistics.getMenu()
            );
        }
    }
}
