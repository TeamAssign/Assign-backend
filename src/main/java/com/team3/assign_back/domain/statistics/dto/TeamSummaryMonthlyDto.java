package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamSummaryMonthlyDto {
    private Long teamId;
    private int year;
    private int month;
    private int day;
    private TeamSummaryMonthly.Statistics statistics;

    public static TeamSummaryMonthlyDto fromEntity(TeamSummaryMonthly entity) {
        return new TeamSummaryMonthlyDto(
                entity.getTeamId(),
                entity.getYear(),
                entity.getMonth(),
                entity.getDay(),
                entity.getStatistics()
        );
    }
}
