package com.team3.assign_back.domain.food.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.team3.assign_back.domain.food.entity.QTasteMetricsEmbedding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class CustomTasteMetricsEmbeddingRepositoryImpl implements CustomTasteMetricsEmbeddingRepository {

    private final JPQLQueryFactory query;
    private static final QTasteMetricsEmbedding tasteMetricsEmbedding = QTasteMetricsEmbedding.tasteMetricsEmbedding;

    @Override
    public float[] findTextEmbeddingByTasteMetricsId(Long tasteMetricsId) {

        return query
                .from(tasteMetricsEmbedding)
                .where(tasteMetricsEmbedding.tasteMetricsId.eq(tasteMetricsId))
                .select(tasteMetricsEmbedding.textEmbedding)
                .fetchFirst();

    }
}
