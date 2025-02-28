package com.team3.assign_back.domain.food.repository;


public interface CustomTasteMetricsEmbeddingRepository{

    float[] findTextEmbeddingByTasteMetricsId(Long tasteMetricsId);
}
