package com.team3.assign_back.domain.statistics.service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import com.team3.assign_back.domain.statistics.dto.*;
import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.domain.statistics.repository.CompanySummaryRepository;
import com.team3.assign_back.domain.statistics.repository.CustomSummaryQueryRepository;
import com.team3.assign_back.domain.statistics.repository.TeamSummaryRepository;
import com.team3.assign_back.domain.statistics.repository.UserSummaryRepository;

import com.team3.assign_back.global.exception.custom.CustomException;
import com.team3.assign_back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final UserSummaryRepository userSummaryRepository;
    private final TeamSummaryRepository teamSummaryRepository;
    private final CompanySummaryRepository companySummaryRepository;
    private final CustomSummaryQueryRepository summaryQueryRepository;
    private final MongoTemplate mongoTemplate;

    // 유저별 최근 3달 통계(기준 당일) 생성
    @Transactional
    public List<UserSummaryMonthly> saveAllUserSummaries() {
        LocalDate today = LocalDate.now();
        LocalDate MonthsAgo = today.minus(90, ChronoUnit.DAYS);


        List<UserReviewSummaryDto> directReviewResults = summaryQueryRepository.fetchDirectReviewForUser(MonthsAgo, today);
        List<UserReviewSummaryDto> recommendationReviewResults = summaryQueryRepository.fetchRecommendationReviewForUser(MonthsAgo, today);

        //개인별 그룹화
        Map<Long, UserSummaryMonthly.Statistics> statisticsByUser = Stream.concat(directReviewResults.stream(), recommendationReviewResults.stream())
                .collect(Collectors.groupingBy(
                        UserReviewSummaryDto::getUserId,
                        Collectors.collectingAndThen(Collectors.toList(), this::aggregateUserStatistics)
                ));

        List<UserSummaryMonthly> summaries = statisticsByUser.entrySet().stream()
                .map(entry -> UserSummaryMonthly.builder()
                        .userId(entry.getKey())
                        .year(today.getYear())
                        .month(today.getMonthValue())
                        .day(today.getDayOfMonth())
                        .statistics(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        mongoTemplate.insertAll(summaries);
        return summaries;
    }

    // 팀별 최근 한달 통계 생성(기준 당일)
    @Transactional
    public List<TeamSummaryMonthly> saveAllTeamSummaries() {

        LocalDate today = LocalDate.now();
        LocalDate MonthAgo = today.minus(30, ChronoUnit.DAYS);

        List<TeamReviewSummaryDto> directReviewResults = summaryQueryRepository.fetchDirectReviewForTeam(MonthAgo, today);
        List<TeamReviewSummaryDto> recommendationReviewResults = summaryQueryRepository.fetchRecommendationReviewForTeam(MonthAgo, today);

        //팀별 그룹화
        Map<Long, TeamSummaryMonthly.Statistics> statisticsByTeam = Stream.concat(directReviewResults.stream(), recommendationReviewResults.stream())
                .collect(Collectors.groupingBy(
                        TeamReviewSummaryDto::getTeamId,
                        Collectors.collectingAndThen(Collectors.toList(), this::aggregateTeamStatistics)
                ));

        List<TeamSummaryMonthly> summaries = statisticsByTeam.entrySet().stream()
                .map(entry -> TeamSummaryMonthly.builder()
                        .teamId(entry.getKey())
                        .year(today.getYear())
                        .month(today.getMonthValue())
                        .day(today.getDayOfMonth())
                        .statistics(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        mongoTemplate.insertAll(summaries);
        return summaries;
    }

    // 전사 최근 한달 통계 생성(기준 당일)
    @Transactional
    public CompanySummaryMonthly saveCompanySummary() {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);

        Query query = new Query();
        query.addCriteria(Criteria.where("year").gte(oneMonthAgo.getYear()).lte(today.getYear())
                .and("month").gte(oneMonthAgo.getMonthValue()).lte(today.getMonthValue())
                .and("day").gte(oneMonthAgo.getDayOfMonth()).lte(today.getDayOfMonth()));

        List<UserSummaryMonthly> userSummaries = mongoTemplate.find(query, UserSummaryMonthly.class);

        int totalCount = 0;
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (UserSummaryMonthly userSummary : userSummaries) {
            totalCount += userSummary.getStatistics().getTotalCount();
            for (Map.Entry<String, Integer> entry : userSummary.getStatistics().getCategories().entrySet()) {
                categoryCounts.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        CompanySummaryMonthly.Statistics statistics = new CompanySummaryMonthly.Statistics();
        statistics.setTotalCount(totalCount);
        statistics.setCategories(categoryCounts);

        CompanySummaryMonthly summary = CompanySummaryMonthly.builder()
                .year(today.getYear())
                .month(today.getMonthValue())
                .day(today.getDayOfMonth())
                .statistics(statistics)
                .build();

        return companySummaryRepository.save(summary);
    }

    public UserSummaryMonthlyDto getLatestUserSummary(long userId) {
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "year", "month", "day"))
                .limit(1);

        UserSummaryMonthly result = mongoTemplate.findOne(query, UserSummaryMonthly.class);
        if (result == null) { throw new CustomException(ErrorCode.USER_SUMMARY_NOT_FOUND); }
        return UserSummaryMonthlyDto.fromEntity(result);
    }

    public TeamSummaryMonthlyDto getLatestTeamSummary(long teamId) {
        Query query = new Query(Criteria.where("teamId").is(teamId))
                .with(Sort.by(Sort.Direction.DESC, "year", "month", "day"))
                .limit(1);

        TeamSummaryMonthly result = mongoTemplate.findOne(query, TeamSummaryMonthly.class);
        if (result == null) { throw new CustomException(ErrorCode.TEAM_SUMMARY_NOT_FOUND); }
        return TeamSummaryMonthlyDto.fromEntity(result);
    }

    public CompanySummaryMonthlyDto getLatestCompanySummary() {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "year", "month", "day"))
                .limit(1);

        CompanySummaryMonthly result = mongoTemplate.findOne(query, CompanySummaryMonthly.class);
        if (result == null) { throw new CustomException(ErrorCode.COMPANY_SUMMARY_NOT_FOUND); }
        return CompanySummaryMonthlyDto.fromEntity(result);
    }

    private UserSummaryMonthly.Statistics aggregateUserStatistics(List<UserReviewSummaryDto> dtos) {
        Map<String, Integer> categoryCounts = dtos.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.getCategory().name(),
                        Collectors.summingInt(dto -> (int) dto.getCount())
                ));

        int totalCount = categoryCounts.values().stream().mapToInt(Integer::intValue).sum();

        return new UserSummaryMonthly.Statistics(totalCount, categoryCounts);
    }
    private TeamSummaryMonthly.Statistics aggregateTeamStatistics(List<TeamReviewSummaryDto> dtos) {
        Map<String, Integer> categoryCounts = dtos.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.getCategory().name(),
                        Collectors.summingInt(dto -> (int) dto.getCount())
                ));

        Map<String, Integer> menuCounts = dtos.stream()
                .collect(Collectors.groupingBy(
                        TeamReviewSummaryDto::getFoodName,
                        Collectors.summingInt(dto -> (int) dto.getCount())
                ));

        int totalCount = categoryCounts.values().stream().mapToInt(Integer::intValue).sum();

        return new TeamSummaryMonthly.Statistics(totalCount, categoryCounts, menuCounts);
    }
}
