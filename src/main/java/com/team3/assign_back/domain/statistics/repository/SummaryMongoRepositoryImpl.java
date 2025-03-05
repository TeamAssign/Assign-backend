package com.team3.assign_back.domain.statistics.repository;

import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SummaryMongoRepositoryImpl implements SummaryMongoRepository {
    private final MongoTemplate mongoTemplate;

    // 유저 최신 데이터 조회
    @Override
    public UserSummaryMonthly findLatestUserSummary(long userId) {
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "year", "month", "day", "createdAt"))
                .limit(1);

        UserSummaryMonthly result = mongoTemplate.findOne(query, UserSummaryMonthly.class);
        if (result == null) throw new CustomException(ErrorCode.USER_SUMMARY_NOT_FOUND);
        return result;
    }

    // 팀 최신 데이터 조회
    @Override
    public TeamSummaryMonthly findLatestTeamSummary(long teamId) {
        Query query = new Query(Criteria.where("teamId").is(teamId))
                .with(Sort.by(Sort.Direction.DESC, "year", "month", "day", "createdAt"))
                .limit(1);

        TeamSummaryMonthly result = mongoTemplate.findOne(query, TeamSummaryMonthly.class);
        if (result == null) throw new CustomException(ErrorCode.TEAM_SUMMARY_NOT_FOUND);
        return result;
    }

    // 전사 최신 데이터 조회
    @Override
    public CompanySummaryMonthly findLatestCompanySummary() {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "year", "month", "day", "createdAt"))
                .limit(1);

        CompanySummaryMonthly result = mongoTemplate.findOne(query, CompanySummaryMonthly.class);
        if (result == null) throw new CustomException(ErrorCode.COMPANY_SUMMARY_NOT_FOUND);
        return result;
    }

    // 전사 통계 데이터 조회 (특정 기간)
    @Override
    public List<UserSummaryMonthly> findUserSummariesForCompany(LocalDate today) {
        LocalDate oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);

        Query query = new Query();
        query.addCriteria(Criteria.where("year").gte(oneMonthAgo.getYear()).lte(today.getYear())
                .and("month").gte(oneMonthAgo.getMonthValue()).lte(today.getMonthValue())
                .and("day").gte(oneMonthAgo.getDayOfMonth()).lte(today.getDayOfMonth()));

        return mongoTemplate.find(query, UserSummaryMonthly.class);
    }

    @Override
    public UserRecommendationStats findLatestUserPreferenceSummary(long userId) {
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "year", "month","createdAt"))
                .limit(1);

        UserRecommendationStats result = mongoTemplate.findOne(query, UserRecommendationStats.class);
        if (result == null) throw new CustomException(ErrorCode.USER_SUMMARY_NOT_FOUND);
        return result;
    }

    @Override
    public void deleteExistingUserSummaries(int year, int month, int day) {
        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year)
                .and("month").is(month)
                .and("day").is(day));

        mongoTemplate.remove(query, UserSummaryMonthly.class);
    }

}
