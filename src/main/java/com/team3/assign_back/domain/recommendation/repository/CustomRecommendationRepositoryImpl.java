package com.team3.assign_back.domain.recommendation.repository;


import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.team3.assign_back.global.constant.RecommendationConstant.RECOMMENDATION_QUERY_LIMIT_COUNT;

@Repository
public class CustomRecommendationRepositoryImpl implements CustomRecommendationRepository {

    @PersistenceContext
    private EntityManager entityManager;

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
                WHERE f.category = ?2 AND f.id NOT IN (?4)
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
            WHERE f.category = ?2 AND f.id NOT IN (?4)
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
                    (2 -(tme.text_embedding <=> tpe.like_embedding) + (tme.text_embedding <=> tpe.dislike_embedding)) / 4 as similarity
                FROM food f
                JOIN taste_metrics tm ON tm.food_id = f.id
                JOIN taste_metrics_embedding tme ON tme.taste_metrics_id = tm.food_id
                JOIN users u ON u.id =?1
                JOIN team t ON u.team_id = t.id
                JOIN team_taste_preference ttp ON ttp.team_id = u.id
                JOIN taste_preference tp ON ttp.taste_preference_id = tp.id
                JOIN taste_preference_embedding tpe ON tpe.taste_preference_id = tp.id
                WHERE f.category = ?2 AND f.id NOT IN (?4)
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

}
