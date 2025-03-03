package com.team3.assign_back.domain.review.repository;

import com.team3.assign_back.domain.review.entity.DirectReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectReviewRepository extends JpaRepository<DirectReview, Long> {
}