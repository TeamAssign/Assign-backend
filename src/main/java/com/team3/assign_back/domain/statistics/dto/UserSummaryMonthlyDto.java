package com.team3.assign_back.domain.statistics.dto;

import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryMonthlyDto {
    private Long userId;
    private int year;
    private int month;
    private int day;
    private UserSummaryMonthly.Statistics statistics;

    public static UserSummaryMonthlyDto fromEntity(UserSummaryMonthly entity) {
        return new UserSummaryMonthlyDto(
                entity.getUserId(),
                entity.getYear(),
                entity.getMonth(),
                entity.getDay(),
                entity.getStatistics()
        );
    }
}

