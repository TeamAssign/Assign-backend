package com.team3.assign_back.domain.statistics.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "company_summary_monthly")
@CompoundIndexes({@CompoundIndex(name = "year_month_day_idx", def = "{'year': 1, 'month': 1, 'day' : 1}")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CompanySummaryMonthly {
    @Id
    private String id;
    private int year;
    private int month;
    private int day;

    private Statistics statistics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statistics {
        private int totalCount;
        private Map<String, Integer> categories;
    }
}

