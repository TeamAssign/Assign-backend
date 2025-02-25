package com.team3.assign_back.domain.recommendation.repository;


import com.team3.assign_back.domain.food.dto.FoodResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class CustomRecommendationRepositoryImpl implements CustomRecommendationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, List<Long> participants) {
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
                WHERE f.category = ?2
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
        query.setParameter(3, 1);

        Object[] results = (Object[]) query.getSingleResult();



        return new FoodResponseDto(
                (String) results[0],
                (String) results[1],
                new BigDecimal(String.format("%.3f", ((double) results[2])/participants.size())));
    }

    public FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category) {
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
            WHERE f.category = ?2
            ORDER BY similarity DESC
            LIMIT ?3
            """;

        Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter(1, userId);
        query.setParameter(2, category.name());
        query.setParameter(3, 1);

        Object[] results = (Object[]) query.getSingleResult();

        return new FoodResponseDto(
                        (String) results[0],
                        (String) results[1],
                new BigDecimal(String.format("%.3f", (double) results[2])));
    }



    @Override
    public FoodResponseDto getRecommendationForTeam(Long userId, FoodEnum.FoodCategory category) {
        String nativeQuery = """
            SELECT
                f.name,
                f.img_url,
                (2 -(tme.text_embedding <=> tpe.like_embedding) + (tme.text_embedding <=> tpe.dislike_embedding)) / 4 as similarity
            FROM food f
            JOIN taste_metrics tm ON tm.food_id = f.id
            JOIN taste_metrics_embedding tme ON tme.taste_metrics_id = tm.food_id
            JOIN users u ON u.id =?1
            JOIN team t ON u.team_id = t.id
            JOIN team_taste_preference ttp ON ttp.team_id = u.id
            JOIN taste_preference tp ON ttp.taste_preference_id = tp.id
            JOIN taste_preference_embedding tpe ON tpe.taste_preference_id = tp.id
            WHERE f.category = ?2
            ORDER BY similarity DESC
            LIMIT ?3
            """;

        Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter(1, userId);
        query.setParameter(2, category.name());
        query.setParameter(3, 1);

        Object[] results = (Object[]) query.getSingleResult();

        return new FoodResponseDto(
                (String) results[0],
                (String) results[1],
                new BigDecimal(String.format("%.3f", (double) results[2])));
    }




//
//
//    private final JPAQueryFactory query;
//
//    private final QTastePreferenceEmbedding tastePreferenceEmbedding = QTastePreferenceEmbedding.tastePreferenceEmbedding;
//    private final QTastePreference tastePreference = QTastePreference.tastePreference;
//    private final QUserTastePreference userTastePreference = QUserTastePreference.userTastePreference;
//    private final QUsers users = QUsers.users;
//    private final QFood food = QFood.food;
//    private final QTasteMetricsEmbedding tasteMetricsEmbedding = QTasteMetricsEmbedding.tasteMetricsEmbedding;
//    private final QTasteMetrics tasteMetrics = QTasteMetrics.tasteMetrics;
//
//    @Override
//    public FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, FoodEnum.FoodType type, List<Long> participants) {
//
//        NumberExpression<Double> similarity = cosineSimilarityExpression(tasteMetricsEmbedding.textEmbedding, getUserEmbeddingSubQuery(1L));
//
//        NumberExpression<BigDecimal> similarityBigDecimal = Expressions.numberTemplate(
//                BigDecimal.class,
//                "CAST({0} AS DECIMAL(3,3))",
//                similarity
//        );
//
//        return query
//                .select(Projections.constructor(FoodResponseDto.class, food.name, food.imgUrl, similarityBigDecimal))
//                .from(food)
//                .join(tasteMetrics)
//                .on(tasteMetrics.food.eq(food))
//                .join(tasteMetricsEmbedding)
//                .on(tasteMetricsEmbedding.tasteMetrics.eq(tasteMetrics))
//                .where(food.category.eq(FoodEnum.FoodCategory.KOREAN))
//                .orderBy(similarity.asc()).limit(1).fetchFirst();
//
//
//    }
//
//    private NumberExpression<Double> cosineSimilarityExpression(ArrayPath<float[], Float> embedding, JPQLQuery<float[]> subQuery) {
//        return Expressions.numberTemplate(
//                Double.class,
//                "1 - ({0} <=> ({1})) / 2",
//                embedding,
//                subQuery
//        );
//    }
//
//    private NumberExpression<Double> cosineSimilarityExpression(ArrayPath<float[], Float> embedding1, ArrayPath<float[], Float> embedding2) {
//        return Expressions.numberTemplate(
//                Double.class,
//                "{0} <=> {1}",
//                embedding1,
//                embedding2
//        );
//    }
//
//    private JPQLQuery<float[]> getUserEmbeddingSubQuery(Long userId) {
//        return JPAExpressions.select(tastePreferenceEmbedding.textEmbedding)
//                .from(users)
//                .join(userTastePreference)
//                .on(userTastePreference.users.eq(users))
//                .join(tastePreference)
//                .on(userTastePreference.tastePreference.eq(tastePreference))
//                .join(tastePreferenceEmbedding)
//                .on(tastePreferenceEmbedding.tastePreference.eq(tastePreference))
//                .select(tastePreferenceEmbedding.textEmbedding)
//                .where(users.id.eq(userId)).limit(1L);
//    }
}
