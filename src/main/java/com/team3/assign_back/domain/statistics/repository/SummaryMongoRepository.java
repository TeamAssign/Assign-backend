package com.team3.assign_back.domain.statistics.repository;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;

import java.time.LocalDate;
import java.util.List;

public interface SummaryMongoRepository {

    UserSummaryMonthly findLatestUserSummary(long userId);
    TeamSummaryMonthly findLatestTeamSummary(long teamId);
    CompanySummaryMonthly findLatestCompanySummary();
    List<UserSummaryMonthly> findUserSummariesForCompany(LocalDate today);
    UserRecommendationStats findLatestUserPreferenceSummary(long userId);

    void deleteExistingUserSummaries(int year, int month, int day);
}
