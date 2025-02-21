package com.team3.assign_back.domain.food.repository;

import com.team3.assign_back.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByName(String foodName);

    @Query("SELECT f FROM Food f LEFT JOIN TasteMetrics t ON f.id = t.id WHERE t.id IS NULL")
    List<Food> customFindFoodsWithoutTasteMetrics();
}
