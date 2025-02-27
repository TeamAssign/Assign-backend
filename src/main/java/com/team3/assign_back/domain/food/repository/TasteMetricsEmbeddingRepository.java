package com.team3.assign_back.domain.food.repository;

import com.team3.assign_back.domain.food.entity.TasteMetricsEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TasteMetricsEmbeddingRepository extends JpaRepository<TasteMetricsEmbedding, Long> {

    @Query("SELECT t.textEmbedding FROM TasteMetricsEmbedding t WHERE t.tasteMetricsId = :id")
    float[] findTextEmbeddingByTasteMetricsId(@Param("id") Long tasteMetricsId);
}
