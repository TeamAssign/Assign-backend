package com.team3.assign_back.domain.statistics.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.assign_back.domain.food.entity.QFood;
import com.team3.assign_back.domain.intermediate.entity.QParticipant;
import com.team3.assign_back.domain.recommendation.entity.QRecommendation;
import com.team3.assign_back.domain.review.entity.QDirectReview;
import com.team3.assign_back.domain.review.entity.QRecommendationReview;
import com.team3.assign_back.domain.review.entity.QReview;
import com.team3.assign_back.domain.statistics.dto.TeamReviewSummaryDto;
import com.team3.assign_back.domain.statistics.dto.UserReviewSummaryDto;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.entity.QUsers;
import com.team3.assign_back.global.enums.FoodEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomSummaryQueryRepositoryImpl implements CustomSummaryQueryRepository {
    private final JPAQueryFactory queryFactory;
    private static final QReview review = QReview.review;
    private static final QUsers users = QUsers.users;
    private static final QTeam team = QTeam.team;
    private static final QFood food = QFood.food;
    private static final QDirectReview directReview = QDirectReview.directReview;
    private static final QRecommendationReview recommendationReview = QRecommendationReview.recommendationReview;
    private static final QRecommendation recommendation = QRecommendation.recommendation;
    private static final QParticipant participant = QParticipant.participant;
    @Override
    public List<UserReviewSummaryDto> fetchDirectReviewForUser(LocalDate start, LocalDate end) {

        return queryFactory.select(
                        Projections.constructor(UserReviewSummaryDto.class, users.id, directReview.createdAt,
                                food.category, food.name, review.count()))
                .from(directReview)
                .join(review).on(directReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(food).on(directReview.food.id.eq(food.id))
                .where(dateBetween(directReview.createdAt, start, end))
                .groupBy(users.id, directReview.createdAt, food.category, food.name)
                .fetch();
    }

    @Override
    public List<UserReviewSummaryDto> fetchRecommendationReviewForUser(LocalDate start, LocalDate end) {

        return queryFactory.select(
                Projections.constructor(UserReviewSummaryDto.class,
                        users.id, recommendationReview.createdAt,
                        food.category, food.name, review.count()))
                .from(recommendationReview)
                .join(review).on(recommendationReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(recommendation).on(recommendationReview.recommendation.id.eq(recommendation.id))
                .join(food).on(recommendation.food.id.eq(food.id))
                .where(dateBetween(recommendationReview.createdAt, start, end))
                .groupBy(users.id, recommendationReview.createdAt, food.category, food.name)
                .fetch();
    }

    @Override
    public List<TeamReviewSummaryDto> fetchDirectReviewForTeam(LocalDate start, LocalDate end){

        return  queryFactory
                .select(Projections.constructor(TeamReviewSummaryDto.class,
                        team.id, directReview.createdAt,
                        food.category, food.name, review.id.countDistinct() // 중복 제거된 리뷰 개수
                ))
                .from(directReview)
                .join(review).on(directReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(team).on(users.team.id.eq(team.id))
                .join(food).on(directReview.food.id.eq(food.id))
                .leftJoin(participant).on(participant.review.id.eq(review.id))
                .where(
                        dateBetween(directReview.createdAt, start, end),
                        isGroupOrCompanyDinner(directReview.type),
                        isValidTeamReview(review.id, directReview.type, team.id)

                )
                .groupBy(team.id, directReview.createdAt,
                        food.category, food.name)
                .fetch();
    }

    @Override
    public List<TeamReviewSummaryDto> fetchRecommendationReviewForTeam(LocalDate start, LocalDate end){

        return queryFactory
                .select(Projections.constructor(TeamReviewSummaryDto.class,team.id, recommendationReview.createdAt,
                        food.category, food.name, review.id.countDistinct() // 중복 제거된 리뷰 개수
                ))
                .from(recommendationReview)
                .join(review).on(recommendationReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(team).on(users.team.id.eq(team.id))
                .join(recommendation).on(recommendationReview.recommendation.id.eq(recommendation.id))
                .join(food).on(recommendation.food.id.eq(food.id))
                .leftJoin(participant).on(participant.review.id.eq(review.id))
                .where(
                        dateBetween(recommendationReview.createdAt, start, end),
                        isGroupOrCompanyDinner(recommendationReview.recommendation.type),
                        isValidTeamReview(review.id, recommendationReview.recommendation.type, team.id)
                )
                .groupBy(team.id, recommendationReview.createdAt,
                        food.category, food.name)
                .fetch();
    }


    private BooleanExpression dateBetween(DateTimePath<LocalDateTime> target, LocalDate start, LocalDate end) {
        return target.between(start.atStartOfDay(), end.atTime(23, 59, 59));
    }

    private BooleanExpression isGroupOrCompanyDinner(EnumPath<FoodEnum.FoodType> type) {
        return type.in(FoodEnum.FoodType.GROUP, FoodEnum.FoodType.COMPANYDINNER);
    }

    private BooleanExpression isValidTeamReview(NumberPath<Long> reviewId, EnumPath<FoodEnum.FoodType> type, NumberPath<Long> teamId) {
        BooleanExpression isGroupReview = type.eq(FoodEnum.FoodType.GROUP)
                .and(queryFactory.select(participant.id.count()).from(participant)
                        .where(participant.review.id.eq(reviewId))
                        .gt(0L))
                .and(queryFactory.select(participant.users.team.id.countDistinct()).from(participant)
                        .where(participant.review.id.eq(reviewId))
                        .eq(1L))
                .and(queryFactory.select(participant.users.team.id.max()).from(participant)
                        .where(participant.review.id.eq(reviewId))
                        .eq(queryFactory.select(users.team.id).from(users).where(users.id.eq(review.users.id))));

        BooleanExpression isCompanyDinnerReview = type.eq(FoodEnum.FoodType.COMPANYDINNER)
                .and(queryFactory.select(participant.id.count()).from(participant)
                        .where(participant.review.id.eq(reviewId))
                        .eq(0L))
                .and(queryFactory.select(users.team.id).from(users)
                        .where(users.id.eq(review.users.id))
                        .eq(teamId));

        return isGroupReview.or(isCompanyDinnerReview);
    }
}
