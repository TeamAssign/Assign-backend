package com.team3.assign_back.domain.statistics.repository;

import com.querydsl.core.Tuple;
import com.team3.assign_back.domain.statistics.dto.TeamReviewSummaryDto;
import com.team3.assign_back.domain.statistics.dto.UserReviewSummaryDto;

import java.time.LocalDate;
import java.util.List;

public interface CustomSummaryQueryRepository {
    List<UserReviewSummaryDto> fetchDirectReviewForUser(LocalDate start, LocalDate end);
    List<UserReviewSummaryDto> fetchRecommendationReviewForUser(LocalDate start, LocalDate end);

    List<TeamReviewSummaryDto> fetchDirectReviewForTeam(LocalDate start, LocalDate end);
    List<TeamReviewSummaryDto> fetchRecommendationReviewForTeam(LocalDate start, LocalDate end);


}
