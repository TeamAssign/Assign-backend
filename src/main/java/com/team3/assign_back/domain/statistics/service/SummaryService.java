package com.team3.assign_back.domain.statistics.service;
import com.team3.assign_back.domain.statistics.entity.UserRecommendationStats;
import com.team3.assign_back.domain.recommendation.repository.CustomRecommendationRepository;
import com.team3.assign_back.domain.statistics.repository.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.team3.assign_back.domain.statistics.dto.*;
import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final CompanySummaryRepository companySummaryRepository;
    private final CustomSummaryQueryRepository summaryQueryRepository;
    private final SummaryMongoRepository summaryMongoRepository;
    private final MongoTemplate mongoTemplate;
    private final CustomRecommendationRepository customRecommendationRepository;

    // 유저별 최근 3달 통계(기준 당일) 생성
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
    public CompanySummaryMonthly saveCompanySummary() {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
        List<UserSummaryMonthly> userSummaries = summaryMongoRepository.findUserSummariesForCompany(today);

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

    // 유저별 선호도 분석 데이터
    public List<UserRecommendationStats> saveBatchUserPreferenceStats(){
        List<UserRecommendationStats> userRecommendationStats = customRecommendationRepository.getUserRecommendationStats();
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        List<UserRecommendationStats> updatedStatsList = userRecommendationStats.stream()
                .map(stats -> {
                    stats.setYear(currentYear);
                    stats.setMonth(currentMonth);
                    return stats;
                })
                .collect(Collectors.toList());

        // MongoDB에 저장
        mongoTemplate.insertAll(updatedStatsList);
        return updatedStatsList;
    }

    public UserSummaryMonthlyDto getLatestUserSummary(long userId) {
        return UserSummaryMonthlyDto.fromEntity(summaryMongoRepository.findLatestUserSummary(userId));
    }

    public TeamSummaryMonthlyDto getLatestTeamSummary(long teamId) {
        return TeamSummaryMonthlyDto.fromEntity(summaryMongoRepository.findLatestTeamSummary(teamId));
    }

    public CompanySummaryMonthlyDto getLatestCompanySummary() {
        return CompanySummaryMonthlyDto.fromEntity(summaryMongoRepository.findLatestCompanySummary());
    }

    public UserRecommendationStatsDto getLatestUserPreferenceSummary(long userId){
        return UserRecommendationStatsDto.fromEntity(summaryMongoRepository.findLatestUserPreferenceSummary(userId));
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
