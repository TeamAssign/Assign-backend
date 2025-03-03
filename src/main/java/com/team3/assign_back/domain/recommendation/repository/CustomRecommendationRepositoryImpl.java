package com.team3.assign_back.domain.recommendation.repository;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.assign_back.domain.food.entity.QFood;
import com.team3.assign_back.domain.recommendation.dto.RecommendationHistoryResponseDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.domain.recommendation.entity.QRecommendation;
import com.team3.assign_back.domain.recommendation.entity.QUsersRecommendation;
import com.team3.assign_back.domain.review.entity.QRecommendationReview;
import com.team3.assign_back.domain.review.entity.QReview;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.dto.UserSearchResponseDto;
import com.team3.assign_back.domain.users.entity.QUsers;
import com.team3.assign_back.global.common.PageResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.custom.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.Projections.constructor;
import static com.team3.assign_back.global.constant.RecommendationConstant.RECOMMENDATION_QUERY_LIMIT_COUNT;
import static com.team3.assign_back.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Repository
@RequiredArgsConstructor
public class CustomRecommendationRepositoryImpl implements CustomRecommendationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    private final JPAQueryFactory query;
    private static final QUsers users = QUsers.users;
    private static final QTeam team = QTeam.team;
    private static final QRecommendation recommendation = QRecommendation.recommendation;
    private static final QUsersRecommendation usersRecommendation = QUsersRecommendation.usersRecommendation;
    private static final QRecommendationReview recommendationReview = QRecommendationReview.recommendationReview;
    private static final QReview review = QReview.review;
    private static final QFood food = QFood.food;




    @Override
    public List<RecommendationResponseDto> getRecommendation(List<Long> participants, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds) {
        String nativeQuery = """
            WITH user_similarities AS (
                SELECT
                    f.id,
                    f.name,
                    f.img_url,
                    (2 -(tme.text_embedding <=> user_prefs.like_embedding) + (tme.text_embedding <=> user_prefs.dislike_embedding)) / 4 as similarity
                FROM food f
                JOIN taste_metrics tm ON tm.food_id = f.id
                JOIN taste_metrics_embedding tme ON tme.taste_metrics_id = tm.food_id
                CROSS JOIN (
                    SELECT
                        tpe.like_embedding,
                        tpe.dislike_embedding
                    FROM users u
                    JOIN user_taste_preference utp ON utp.users_id = u.id
                    JOIN taste_preference tp ON utp.taste_preference_id = tp.id
                    JOIN taste_preference_embedding tpe ON tpe.taste_preference_id = tp.id
                    WHERE u.id IN (?1)
                ) AS user_prefs
                WHERE f.category = ?2 AND f.id != ALL(?4)
            )
            SELECT
                name,
                img_url,
                SUM(similarity) as similarity
            FROM user_similarities
            GROUP BY id, name, img_url
            ORDER BY similarity DESC
            LIMIT ?3
            """;

        Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter(1, participants.toArray(Long[]::new));
        query.setParameter(2, category.name());
        query.setParameter(3, RECOMMENDATION_QUERY_LIMIT_COUNT);
        query.setParameter(4, rejectedFoodIds.toArray(Long[]::new));

        @SuppressWarnings("unchecked")
        List<Object[]> results = (List<Object[]>) query.getResultList();

        return results.stream()
                .map(result -> new RecommendationResponseDto(
                        (String) result[0],
                        (String) result[1],
                        new BigDecimal(String.format("%.3f", ((double) result[2])/participants.size()))))
                .toList();

    }

    public List<RecommendationResponseDto> getRecommendation(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds) {
        String nativeQuery = """
            SELECT
                f.name,
                f.img_url,
                (2 -(tme.text_embedding <=> tpe.like_embedding) + (tme.text_embedding <=> tpe.dislike_embedding)) / 4  as similarity
            FROM food f
            JOIN taste_metrics tm ON tm.food_id = f.id
            JOIN taste_metrics_embedding tme ON tme.taste_metrics_id = tm.food_id
            JOIN users u ON u.id =?1
            JOIN user_taste_preference utp ON utp.users_id = u.id
            JOIN taste_preference tp ON utp.taste_preference_id = tp.id
            JOIN taste_preference_embedding tpe ON tpe.taste_preference_id = tp.id
            WHERE f.category = ?2 AND f.id != ALL(?4)
            ORDER BY similarity DESC
            LIMIT ?3
            """;

        Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter(1, userId);
        query.setParameter(2, category.name());
        query.setParameter(3, RECOMMENDATION_QUERY_LIMIT_COUNT);
        query.setParameter(4, rejectedFoodIds.toArray(Long[]::new));

        @SuppressWarnings("unchecked")
        List<Object[]> results = (List<Object[]>) query.getResultList();

        return results.stream()
                .map(result -> new RecommendationResponseDto(
                        (String) result[0],
                        (String) result[1],
                        new BigDecimal(String.format("%.3f", (double) result[2] ))))
                .toList();
    }



    @Override
    public List<RecommendationResponseDto> getRecommendationForTeam(Long userId, FoodEnum.FoodCategory category, List<Long> rejectedFoodIds) {
        String nativeQuery = """
                SELECT
                    f.name,
                    f.img_url,
                    (2 -(tme.text_for_company_dinner_embedding <=> tpe.like_embedding) + (tme.text_for_company_dinner_embedding <=> tpe.dislike_embedding)) / 4 as similarity
                FROM food f
                JOIN taste_metrics tm ON tm.food_id = f.id
                JOIN taste_metrics_embedding tme ON tme.taste_metrics_id = tm.food_id
                JOIN users u ON u.id =?1
                JOIN team t ON u.team_id = t.id
                JOIN team_taste_preference ttp ON ttp.team_id = u.id
                JOIN taste_preference tp ON ttp.taste_preference_id = tp.id
                JOIN taste_preference_embedding tpe ON tpe.taste_preference_id = tp.id
                WHERE f.category = ?2 AND f.id != ALL(?4)
                ORDER BY similarity DESC
                LIMIT ?3
                """;

        Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter(1, userId);
        query.setParameter(2, category.name());
        query.setParameter(3, RECOMMENDATION_QUERY_LIMIT_COUNT);
        query.setParameter(4, rejectedFoodIds.toArray(Long[]::new));

        @SuppressWarnings("unchecked")
        List<Object[]> results = (List<Object[]>) query.getResultList();

        return results.stream()
                .map(result -> new RecommendationResponseDto(
                        (String) result[0],
                        (String) result[1],
                        new BigDecimal(String.format("%.3f", (double) result[2]))))
                .toList();
    }


    @Override
    public void batchSaveUsersRecommendation(Long recommendationId, List<Long> participantIds){
        String sql = "INSERT INTO users_recommendation (users_id, recommendation_id,  created_at, updated_at) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, participantIds.get(i));
                ps.setLong(2, recommendationId);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            }

            @Override
            public int getBatchSize() {
                return participantIds.size();
            }
        });
    }


    @Override
    public PageResponseDto<RecommendationHistoryResponseDto> getRecommendationHistories(Long userId, Pageable pageable) {

        JPAQuery<?> paginationQuery = getPaginationQuery(userId);

        CompletableFuture<Long> total = CompletableFuture.supplyAsync(()-> paginationQuery
                .select(recommendation.count())
                .where(users.id.eq(userId)).fetchFirst());


        List<Long> recommendationIds = paginationQuery
                .select(recommendation.id)
                .orderBy(recommendation.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();



        List<RecommendationHistoryResponseDto> results = query
                .from(recommendation)
                .join(usersRecommendation)
                .on(usersRecommendation.recommendation.eq(recommendation))
                .join(users)
                .on(usersRecommendation.user.eq(users))
                .join(users.team, team)
                .leftJoin(recommendationReview)
                .on(recommendationReview.recommendation.eq(recommendation))
                .join(review)
                .on(recommendationReview.review.eq(review).and(review.users.id.eq(userId)))
                .join(food)
                .on(recommendation.food.eq(food))
                .where(recommendation.id.in(recommendationIds))
                .transform(groupBy(recommendation.id).list(constructor(RecommendationHistoryResponseDto.class,
                        recommendation.id,
                        recommendation.type,
                        food.category,
                        food.name,
                        recommendation.accuracy,
                        food.imgUrl,
                        recommendationReview.id.isNotNull(),
                        list(constructor(UserSearchResponseDto.class,
                                users.id,
                                users.name,
                                team.name,
                                users.profileImgUrl
                        ))
                )));


        //이 과정 꼭 넣어야 할지 고민됨.
        for(RecommendationHistoryResponseDto dto : results){
            dto.getParticipants().removeIf(userSearchResponseDto -> userSearchResponseDto.getId().equals(userId));
        }


        try {
            return new PageResponseDto<>(new PageImpl<>(results, pageable, total.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

    }

    private JPAQuery<?> getPaginationQuery(Long userId) {
        return query
                .from(recommendation)
                .join(usersRecommendation)
                .on(usersRecommendation.recommendation.eq(recommendation))
                .join(users)
                .on(usersRecommendation.user.eq(users))
                .where(users.id.eq(userId));
    }

}
