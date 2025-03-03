package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
