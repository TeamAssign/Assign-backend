package com.team3.assign_back.domain.recommendation.repository;

import com.team3.assign_back.domain.recommendation.entity.UsersRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRecommendationRepository extends JpaRepository<UsersRecommendation, Long> {
}
