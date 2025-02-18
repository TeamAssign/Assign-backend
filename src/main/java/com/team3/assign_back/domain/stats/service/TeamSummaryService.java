package com.team3.assign_back.domain.stats.service;

import com.team3.assign_back.domain.stats.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.stats.repository.TeamSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeamSummaryService {

    private final TeamSummaryRepository teamSummaryRepository;

    public void saveMonthlySummary(String teamId, String userId, int year,int month,
                                   int totalCount, Map<String, Integer> categories, int count){
        TeamSummaryMonthly summary = TeamSummaryMonthly.builder()
                .teamId(teamId)
                .year(year)
                .month(month)
                .build();
    }
}
