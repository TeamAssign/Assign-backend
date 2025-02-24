package com.team3.assign_back.domain.food.repository;

import com.team3.assign_back.domain.food.entity.TasteMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TasteMetricsRepository extends JpaRepository<TasteMetrics, Long> {


    @Query("SELECT t FROM TasteMetrics t JOIN FETCH t.food LEFT JOIN TasteMetricsEmbedding e ON t.id = e.id WHERE e.id IS NULL")
    List<TasteMetrics> customFindTasteMetricsWithoutEmbedding();

    Optional<TasteMetrics> findByFoodId(long l);
}
