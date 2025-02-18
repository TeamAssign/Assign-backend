package com.team3.assign_back.domain.stats.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "team_summary_monthly")
@CompoundIndexes({@CompoundIndex(name = "year_month_idx", def = "{'year': 1, 'month': 1}")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TeamSummaryMonthly {
    @Id
    private String id;  // MongoDB에서는 기본적으로 ObjectId 사용 가능
    private String teamId;  // 팀 단위 집계 시 사용 (필요 없으면 null)

    private int year;
    private int month;  // 몇월 통계인지

    private TeamSummaryWeekly.Statistics statistics;

    @Data
    public static class Statistics {
        private int totalCount;  // 총 음식 소비 횟수
        private Map<String, Integer> categories;  // 음식 카테고리별 소비 횟수
        private Map<String, Integer> menu; // 메뉴별 소비 홧수
        private TeamSummaryWeekly.MostEatenFood mostEatenFood;  // 가장 많이 먹은 음식
    }

    @Data
    public static class MostEatenFood {
        private String name;  // 음식 이름
        private int count;    // 먹은 횟수
    }
}
