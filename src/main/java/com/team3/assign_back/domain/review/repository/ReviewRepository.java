package com.team3.assign_back.domain.review.repository;

import com.team3.assign_back.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.users.id = :userId")
    Page<Review> findByUsersReview(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.users.team.id = :teamId")
    Page<Review> findReviewsByTeamId(@Param("teamId") Long teamId, Pageable pageable);



}