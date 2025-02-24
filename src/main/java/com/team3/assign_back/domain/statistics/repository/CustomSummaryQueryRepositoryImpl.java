package com.team3.assign_back.domain.statistics.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.assign_back.domain.food.entity.QFood;
import com.team3.assign_back.domain.intermediate.entity.QParticipant;
import com.team3.assign_back.domain.recommandation.entity.QRecommendation;
import com.team3.assign_back.domain.review.entity.QDirectReview;
import com.team3.assign_back.domain.review.entity.QRecommendationReview;
import com.team3.assign_back.domain.review.entity.QReview;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.entity.QUsers;
import com.team3.assign_back.global.enums.FoodEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomSummaryQueryRepositoryImpl implements CustomSummaryQueryRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Tuple> fetchDirectReviewForUser(LocalDate start, LocalDate end) {
        QReview review = QReview.review;
        QUsers users = QUsers.users;
        QFood food = QFood.food;
        QDirectReview directReview = QDirectReview.directReview;

        return queryFactory.select(users.id, directReview.createdAt.year(), directReview.createdAt.month(), directReview.createdAt.dayOfMonth(),
                        food.category, food.name, review.count())
                .from(directReview)
                .join(review).on(directReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(food).on(directReview.food.id.eq(food.id))
                .where(directReview.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)))
                .groupBy(users.id, directReview.createdAt.year(), directReview.createdAt.month(), directReview.createdAt.dayOfMonth(),
                        food.category, food.name)
                .fetch();
    }

    @Override
    public List<Tuple> fetchRecommendationReviewForUser(LocalDate start, LocalDate end) {
        QReview review = QReview.review;
        QUsers users = QUsers.users;
        QFood food = QFood.food;
        QRecommendationReview recommendationReview = QRecommendationReview.recommendationReview;
        QRecommendation recommendation = QRecommendation.recommendation;

        return queryFactory.select(users.id, recommendationReview.createdAt.year(), recommendationReview.createdAt.month(), recommendationReview.createdAt.dayOfMonth(),
                        food.category, food.name, review.count())
                .from(recommendationReview)
                .join(review).on(recommendationReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(recommendation).on(recommendationReview.recommendation.id.eq(recommendation.id))
                .join(food).on(recommendation.food.id.eq(food.id))
                .where(recommendationReview.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)))
                .groupBy(users.id, recommendationReview.createdAt.year(), recommendationReview.createdAt.month(), recommendationReview.createdAt.dayOfMonth(),
                        food.category, food.name)
                .fetch();
    }
    @Override
    public List<Tuple> fetchDirectReviewForTeam(LocalDate start, LocalDate end){
        QReview review = QReview.review;
        QUsers users = QUsers.users;
        QTeam team = QTeam.team;
        QFood food = QFood.food;
        QDirectReview directReview = QDirectReview.directReview;
        QParticipant participant = QParticipant.participant;

        return  queryFactory
                .select(team.id, directReview.createdAt.year(), directReview.createdAt.month(), directReview.createdAt.dayOfMonth(),
                        food.category, food.name, review.id.countDistinct() // 중복 제거된 리뷰 개수
                )
                .from(directReview)
                .join(review).on(directReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(team).on(users.team.id.eq(team.id))
                .join(food).on(directReview.food.id.eq(food.id))
                .join(participant).on(participant.review.id.eq(review.id))
                .where(
                        directReview.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)),
                        directReview.type.in(FoodEnum.FoodType.GROUP, FoodEnum.FoodType.COMPANYDINNER)
                )
                .groupBy(team.id, directReview.createdAt.year(), directReview.createdAt.month(), directReview.createdAt.dayOfMonth(),
                        food.category, food.name, review.id)
                .having(
                        queryFactory
                                .select(participant.users.team.id.countDistinct()) // 같은 review_id에 대해 참여한 팀 개수 세기
                                .from(participant)
                                .where(participant.review.id.eq(review.id))
                                .eq(1L) // 참여한 팀이 1개인 경우만 포함
                )
                .fetch();

    }

    @Override
    public List<Tuple> fetchRecommendationReviewForTeam(LocalDate start, LocalDate end){
        QReview review = QReview.review;
        QUsers users = QUsers.users;
        QTeam team = QTeam.team;
        QFood food = QFood.food;
        QRecommendationReview recommendationReview = QRecommendationReview.recommendationReview;
        QRecommendation recommendation = QRecommendation.recommendation;
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(
                        team.id,
                        recommendationReview.createdAt.year(),
                        recommendationReview.createdAt.month(),
                        recommendationReview.createdAt.dayOfMonth(),
                        food.category,
                        food.name,
                        review.id.countDistinct() // 중복 제거된 리뷰 개수
                )
                .from(recommendationReview)
                .join(review).on(recommendationReview.review.id.eq(review.id))
                .join(users).on(review.users.id.eq(users.id))
                .join(team).on(users.team.id.eq(team.id))
                .join(recommendation).on(recommendationReview.recommendation.id.eq(recommendation.id))
                .join(food).on(recommendation.food.id.eq(food.id))
                .join(participant).on(participant.review.id.eq(review.id))
                .where(
                        recommendationReview.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)),
                        recommendationReview.recommendation.type.in(FoodEnum.FoodType.GROUP, FoodEnum.FoodType.COMPANYDINNER)
                )
                .groupBy(team.id, recommendationReview.createdAt.year(), recommendationReview.createdAt.month(), recommendationReview.createdAt.dayOfMonth(),
                        food.category, food.name, review.id)
                .having(
                        queryFactory
                                .select(participant.users.team.id.countDistinct()) // 같은 review_id에 대해 참여한 팀 개수 세기
                                .from(participant)
                                .where(participant.review.id.eq(review.id))
                                .eq(1L) // 참여한 팀이 1개인 경우만 포함
                )
                .fetch();
    }
}
