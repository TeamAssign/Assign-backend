package com.team3.assign_back.domain.review.repository;

import com.team3.assign_back.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.users.id = :userId")
    Page<Review> findByUsers_Id(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Review r " +
            "JOIN r.participants p " +
            "JOIN p.users u " +
            "LEFT JOIN RecommendationReview recRev ON recRev.review.id = r.id " +
            "LEFT JOIN recRev.recommendation rec " +
            "LEFT JOIN DirectReview recDirect ON recDirect.review.id = r.id " +
            "WHERE u.team.id = :teamId " +
            "AND (r.id NOT IN (" +
            "   SELECT p1.review.id FROM Participant p1 " +
            "   JOIN p1.users u1 " +
            "   GROUP BY p1.review.id " +
            "   HAVING COUNT(DISTINCT u1.team.id) > 1" +
            ") OR rec.type = 'COMPANYDINNER' OR recDirect.type = 'COMPANYDINNER')")
    Page<Review> findByTeamId(@Param("teamId") Long teamId, Pageable pageable);


    @EntityGraph(attributePaths = {"participants.users"})
    @Query("SELECT r FROM Review r WHERE r.id = :reviewId")
    Optional<Review> findReviewWithParticipants(@Param("reviewId") Long reviewId);
}