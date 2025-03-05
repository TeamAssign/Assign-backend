package com.team3.assign_back.domain.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;


@Document(collection = "user_summary_monthly")
@CompoundIndexes({@CompoundIndex(name = "year_month_day_idx", def = "{'year': 1, 'month': 1, 'day' : 1}")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserSummaryMonthly {
    @Id
    private String id;
    private Long userId;

    private int year;
    private int month;
    private int day;

    private Statistics statistics;

    @Column(updatable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistics {
        private int totalCount;
        private Map<String, Integer> categories;
    }
}

