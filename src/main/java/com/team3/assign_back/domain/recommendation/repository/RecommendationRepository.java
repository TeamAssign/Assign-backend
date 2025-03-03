package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import com.team3.assign_back.global.enums.FoodEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
