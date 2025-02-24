package com.team3.assign_back.domain.statistics.repository;

import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;

public interface CustomSummaryQueryRepository {
    List<Tuple> fetchDirectReviewForUser(LocalDate start, LocalDate end);
    List<Tuple> fetchRecommendationReviewForUser(LocalDate start, LocalDate end);

    List<Tuple> fetchDirectReviewForTeam(LocalDate start, LocalDate end);
    List<Tuple> fetchRecommendationReviewForTeam(LocalDate start, LocalDate end);


}
