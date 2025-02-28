package com.team3.assign_back.domain.recommandation.repository;

import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
