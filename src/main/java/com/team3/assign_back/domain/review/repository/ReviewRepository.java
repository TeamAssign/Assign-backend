package com.team3.assign_back.domain.review.repository;

import com.team3.assign_back.domain.review.entity.Review;
import com.team3.assign_back.global.enums.FoodEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.users.id = :userId")
    Page<Review> findByUsers_Id(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Review r " +
            "LEFT JOIN DirectReview dr ON r.id = dr.review.id " +
            "LEFT JOIN RecommendationReview rr ON r.id = rr.review.id " +
            "LEFT JOIN rr.recommendation rec " +
            "JOIN r.users u " +
            "JOIN u.team t " +
            "LEFT JOIN Participant p ON p.review.id = r.id " +
            "WHERE (" +
            "   (dr.id IS NOT NULL AND dr.type = 'GROUP') " +
            "   OR (rr.id IS NOT NULL AND rec.type = 'GROUP') " +
            ") " +
            "GROUP BY r, u.team.id " +
            "HAVING COUNT(DISTINCT p.users.team.id) = 1 " +
            "   AND MAX(p.users.team.id) = :teamId " +
            "   AND MAX(u.team.id) = :teamId")
    Page<Review> findGroupReviews(
            @Param("teamId") Long teamId,
            Pageable pageable);

    @Query("SELECT DISTINCT r FROM Review r " +
            "LEFT JOIN DirectReview dr ON r.id = dr.review.id " +
            "LEFT JOIN RecommendationReview rr ON r.id = rr.review.id " +
            "LEFT JOIN rr.recommendation rec " +
            "JOIN r.users u " +
            "JOIN u.team t " +
            "WHERE (" +
            "   (dr.id IS NOT NULL AND dr.type = 'COMPANYDINNER') " +
            "   OR (rr.id IS NOT NULL AND rec.type = 'COMPANYDINNER') " +
            ") " +
            "AND u.team.id = :teamId ")
    Page<Review> findByTeams(
            @Param("teamId") Long teamId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"participants.users"})
    @Query("SELECT r FROM Review r WHERE r.id = :reviewId")
    Optional<Review> findReviewWithParticipants(@Param("reviewId") Long reviewId);
}