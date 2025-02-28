package com.team3.assign_back.domain.food.repository;

import com.team3.assign_back.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("SELECT f.name FROM Food f WHERE f.name IN :names")
    List<String> findNamesNotIn(@Param("names") List<String> names);


    @Query("SELECT f FROM Food f LEFT JOIN TasteMetrics t ON f.id = t.foodId WHERE t.foodId IS NULL")
    List<Food> customFindFoodsWithoutTasteMetrics();

    Optional<Food> findByName(String menu);
}
