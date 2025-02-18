package com.team3.assign_back.domain.stats.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "company_summary_monthly")
@CompoundIndexes({@CompoundIndex(name = "year_month_idx", def = "{'year': 1, 'month': 1}")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CompanySummaryMonthly {
    @Id
    private String id;
    private int year;
    private int month; // 몇월 통계인지

    private Statistics statistics;

    @Data
    public static class Statistics {
        private int totalCount; // 총 음식 먹은 수
        private Map<String, Integer> categories; // 카테고리별 먹은 수
    }
}

