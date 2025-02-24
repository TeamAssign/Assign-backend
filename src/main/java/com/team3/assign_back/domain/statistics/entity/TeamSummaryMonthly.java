package com.team3.assign_back.domain.statistics.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "team_summary_monthly")
@CompoundIndexes({@CompoundIndex(name = "year_month_day_idx", def = "{'year': 1, 'month': 1, 'day' : 1}")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TeamSummaryMonthly {
    @Id
    private String id;
    private Long teamId;

    private int year;
    private int month;  // 몇월 통계인지
    private int day;
    private Statistics statistics;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistics {
        private int totalCount;  // 총 음식 소비 횟수
        private Map<String, Integer> categories = new HashMap<>();
        private Map<String, Integer> menu = new HashMap<>();
    }
}
