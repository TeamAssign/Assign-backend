package com.team3.assign_back.domain.statistics.service;

import com.querydsl.core.Tuple;
import com.team3.assign_back.domain.food.entity.QFood;

import com.team3.assign_back.domain.review.entity.QReview;
import com.team3.assign_back.domain.statistics.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.domain.statistics.repository.CompanySummaryRepository;
import com.team3.assign_back.domain.statistics.repository.CustomSummaryQueryRepository;
import com.team3.assign_back.domain.statistics.repository.TeamSummaryRepository;
import com.team3.assign_back.domain.statistics.repository.UserSummaryRepository;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.entity.QUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
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

    // 유저별 최근 3달 통계(기준 당일) 생성
    @Transactional
    public List<UserSummaryMonthly> saveAllUserSummaries() {
        LocalDate today = LocalDate.now();
        LocalDate MonthsAgo = today.minus(90, ChronoUnit.DAYS);


        List<Tuple> directReviewResults = summaryQueryRepository.fetchDirectReviewForUser(MonthsAgo, today);
        List<Tuple> recommendationReviewResults = summaryQueryRepository.fetchRecommendationReviewForUser(MonthsAgo, today);

        //개인별 그룹화
        Map<Long, UserSummaryMonthly.Statistics> statisticsByUser = Stream.concat(directReviewResults.stream(), recommendationReviewResults.stream())
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(QUsers.users.id),
                        Collectors.collectingAndThen(Collectors.toList(), this::aggregateUserStatistics)
                ));

        return statisticsByUser.entrySet().stream()
                .map(entry -> UserSummaryMonthly.builder()
                        .userId(entry.getKey())
                        .year(today.getYear())
                        .month(today.getMonthValue())
                        .day(today.getDayOfMonth())
                        .statistics(entry.getValue())
                        .build())
                .collect(Collectors.collectingAndThen(Collectors.toList(), userSummaryRepository::saveAll));
    }

    // 팀별 최근 한달 통계 생성(기준 당일)
    @Transactional
    public List<TeamSummaryMonthly> saveAllTeamSummaries() {

        LocalDate today = LocalDate.now();
        LocalDate MonthAgo = today.minus(30, ChronoUnit.DAYS);

        List<Tuple> directReviewResults = summaryQueryRepository.fetchDirectReviewForTeam(MonthAgo, today);
        List<Tuple> recommendationReviewResults = summaryQueryRepository.fetchRecommendationReviewForTeam(MonthAgo, today);

        //팀별 그룹화
        Map<Long, TeamSummaryMonthly.Statistics> statisticsByTeam = Stream.concat(directReviewResults.stream(), recommendationReviewResults.stream())
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(QTeam.team.id),
                        Collectors.collectingAndThen(Collectors.toList(), this::aggregateTeamStatistics)
                ));

        return statisticsByTeam.entrySet().stream()
                .map(entry -> TeamSummaryMonthly.builder()
                        .teamId(entry.getKey())
                        .year(today.getYear())
                        .month(today.getMonthValue())
                        .day(today.getDayOfMonth())
                        .statistics(entry.getValue())
                        .build())
                .collect(Collectors.collectingAndThen(Collectors.toList(), teamSummaryRepository::saveAll));
    }

    // 전사 최근 한달 통계 생성(기준 당일)
    @Transactional
    public CompanySummaryMonthly saveCompanySummary() {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
        List<UserSummaryMonthly> userSummaries = userSummaryRepository.findByYearAndMonthAndDayBetween(
                oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(),
                today.getYear(), today.getMonthValue(), today.getDayOfMonth()
        );
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


    // 특정 유저 최신 통계 조회
    public UserSummaryMonthly getLatestUserSummary(long userId) {
        return userSummaryRepository.findFirstByUserIdOrderByYearDescMonthDescDayDesc(userId);
    }

    // 특정 팀의 최신 통계 조회
    public TeamSummaryMonthly getLatestTeamSummary(long teamId) {
        return teamSummaryRepository.findFirstByTeamIdOrderByYearDescMonthDescDayDesc(teamId);
    }

    // 전사 최신 통계 조회
    public CompanySummaryMonthly getLatestCompanySummary(){
        return companySummaryRepository.findFirstByOrderByYearDescMonthDescDayDesc();
    }

    private <T> T aggregateStatisticsForTuples(List<Tuple> tuples, Function<Tuple, String> categoryExtractor, Function<Tuple, Integer> countExtractor, BiFunction<Integer, Map<String, Integer>, T> statisticsConstructor) {
        Map<String, Integer> categories = tuples.stream()
                .collect(Collectors.groupingBy(
                        categoryExtractor,
                        Collectors.summingInt(countExtractor::apply)
                ));

        int totalCount = categories.values().stream().mapToInt(Integer::intValue).sum();

        return statisticsConstructor.apply(totalCount, categories);
    }

    private UserSummaryMonthly.Statistics aggregateUserStatistics(List<Tuple> tuples) {
        return aggregateStatisticsForTuples(
                tuples,
                tuple -> tuple.get(QFood.food.category).name(),
                tuple -> tuple.get(QReview.review.count()).intValue(),
                UserSummaryMonthly.Statistics::new
        );
    }

    private TeamSummaryMonthly.Statistics aggregateTeamStatistics(List<Tuple> tuples) {
        Map<String, Integer> menuCount = tuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(QFood.food.name),
                        Collectors.summingInt(tuple -> tuple.get(QReview.review.id.countDistinct()).intValue())
                ));

        TeamSummaryMonthly.Statistics stats = aggregateStatisticsForTuples(
                tuples,
                tuple -> tuple.get(QFood.food.category).name(),
                tuple -> tuple.get(QReview.review.id.countDistinct()).intValue(),
                (totalCount, categories) -> new TeamSummaryMonthly.Statistics(totalCount,categories, menuCount)
        );

        return stats;
    }

}
