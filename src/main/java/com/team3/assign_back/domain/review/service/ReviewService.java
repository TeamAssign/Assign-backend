package com.team3.assign_back.domain.review.service;

import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.repository.FoodRepository;
import com.team3.assign_back.domain.intermediate.entity.Participant;
import com.team3.assign_back.domain.intermediate.repository.ParticipantRepository;
import com.team3.assign_back.domain.recommandation.repository.RecommendationRepository;
import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import com.team3.assign_back.domain.review.dto.ReviewRequestDto;
import com.team3.assign_back.domain.review.dto.ReviewResponseDto;
import com.team3.assign_back.domain.review.entity.DirectReview;
import com.team3.assign_back.domain.review.entity.RecommendationReview;
import com.team3.assign_back.domain.review.entity.Review;
import com.team3.assign_back.domain.review.repository.DirectReviewRepository;
import com.team3.assign_back.domain.review.repository.RecommendationReviewRepository;
import com.team3.assign_back.domain.review.repository.ReviewRepository;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.domain.team.repository.TeamRepository;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.domain.users.repository.UserRepository;
import com.team3.assign_back.global.common.PageResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final FoodRepository foodRepository;
    private final RecommendationRepository recommendationRepository;
    private final ReviewRepository reviewRepository;
    private final DirectReviewRepository directReviewRepository;
    private final RecommendationReviewRepository recommendationReviewRepository;

    @Transactional
    public ReviewResponseDto createReview(Long userId, Long teamId, ReviewRequestDto reviewRequestDto) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = null;
        if (teamId != null) {
            team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

            if (!teamId.equals(users.getTeam().getId())) {
                throw new CustomException(ErrorCode.INVALID_TEAM_SELECTION);
            }
        }

        Review review = reviewRepository.save(
                Review.builder()
                        .users(users)
                        .build()
        );

        return (reviewRequestDto.getRecommendationId() != null) ?
                createRecommendationReview(reviewRequestDto, review, users, team) :
                createDirectReview(reviewRequestDto, review, users, team);
    }

    private ReviewResponseDto createDirectReview(ReviewRequestDto reviewRequestDto, Review review, Users users, Team team) {
        Food food = foodRepository.findByName(reviewRequestDto.getMenu())
                .orElseGet(() -> foodRepository.save(
                        Food.builder()
                                .name(reviewRequestDto.getMenu())
                                .category(FoodEnum.FoodCategory.valueOf(reviewRequestDto.getCategory().toUpperCase()))
                                .build()
                ));

        DirectReview directReview = directReviewRepository.save(
                DirectReview.builder()
                        .review(review)
                        .type(FoodEnum.FoodType.valueOf(reviewRequestDto.getType().toUpperCase()))
                        .food(food)
                        .comment(reviewRequestDto.getComment())
                        .star(reviewRequestDto.getStar())
                        .imgUrl(reviewRequestDto.getImgurl())
                        .build()
        );

        saveParticipants(review, reviewRequestDto.getParticipants(), users);

        Long teamId =(team != null) ? team.getId() : null;

        return convertToDirectReviewDTO(directReview, teamId);
    }

    private ReviewResponseDto createRecommendationReview(ReviewRequestDto reviewRequestDto, Review review, Users users, Team team) {
        Recommendation recommendation = recommendationRepository.findById(reviewRequestDto.getRecommendationId())
                .orElseThrow(() -> new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND));

        Food food = recommendation.getFood();
        String foodName = (food != null) ? food.getName() : "기타 음식";
        String foodCategory = (food != null && food.getCategory() != null) ? food.getCategory().name() : "기타";

        RecommendationReview recommendationReview = recommendationReviewRepository.save(
                RecommendationReview.builder()
                        .review(review)
                        .recommendation(recommendation)
                        .comment(reviewRequestDto.getComment())
                        .star(reviewRequestDto.getStar())
                        .imgUrl(reviewRequestDto.getImgurl())
                        .build()
        );

        saveParticipants(review, reviewRequestDto.getParticipants(), users);

        Long teamId = (team != null) ? team.getId() : null;

        return convertToRecommendationReviewDTO(recommendationReview, foodName, foodCategory, recommendation, teamId);
    }

    private void saveParticipants(Review review, List<Long> participantIds, Users users) {
        if (participantIds == null) {
            participantIds = new ArrayList<>();
        }

        if (!participantIds.contains(users.getId())) {
            participantIds.add(users.getId());
        }

        List<Participant> participantList = participantIds.stream()
                .map(participantId -> {
                    Users participantUser = userRepository.findById(participantId)
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    return Participant.builder()
                            .review(review)
                            .users(participantUser)
                            .build();
                })
                .collect(Collectors.toList());

        review.getParticipants().clear();
        review.getParticipants().addAll(participantList);

        participantRepository.saveAll(participantList);
    }

    public PageResponseDto<ReviewResponseDto> getReviewByUser(Long userId , Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Page<Review> reviews = reviewRepository.findByUsersReview(userId, pageable);

        if (reviews.isEmpty()) {
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return new PageResponseDto<>(reviews.map(this::convertToReviewResponse));
    }

    public PageResponseDto<ReviewResponseDto> getReviewByTeam(Long teamId, Pageable pageable) {
        if(!teamRepository.existsById(teamId)){
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }

        Page<Review> teamReviews = reviewRepository.findReviewsByTeamId(teamId, pageable);

        if (teamReviews.isEmpty()) {
            throw new CustomException(ErrorCode.TEAM_REVIEW_NOT_FOUND);
        }

        return new PageResponseDto<>(teamReviews.map(this::convertToReviewResponse));
    }

    private ReviewResponseDto convertToDirectReviewDTO(DirectReview directReview, Long teamId) {
        List<Long> participantIds = directReview.getReview().getParticipants().stream()
                .map(participant -> participant.getUsers().getId())
                .collect(Collectors.toList());

        return ReviewResponseDto.builder()
                .reviewId(directReview.getReview().getId())
                .userId(directReview.getReview().getUsers().getId())
                .type(directReview.getType().name())
                .comment(directReview.getComment())
                .star(directReview.getStar())
                .food(directReview.getFood().getName())
                .category(directReview.getFood().getCategory().getKoreanName())
                .imgurl(directReview.getImgUrl())
                .participants(participantIds)
                .teamId(teamId)
                .build();
    }

    private ReviewResponseDto convertToRecommendationReviewDTO(RecommendationReview recommendationReview, String foodName, String foodCategory, Recommendation recommendation, Long teamId) {
        List<Long> participantIds = recommendationReview.getReview().getParticipants().stream()
                .map(participant -> participant.getUsers().getId())
                .collect(Collectors.toList());

        return ReviewResponseDto.builder()
                .reviewId(recommendationReview.getReview().getId())
                .userId(recommendationReview.getReview().getUsers().getId())
                .recommendationId(recommendationReview.getRecommendation().getId())
                .comment(recommendationReview.getComment())
                .star(recommendationReview.getStar())
                .imgurl(recommendationReview.getImgUrl())
                .type(recommendation.getType().getKoreanName())
                .food(foodName)
                .category(foodCategory)
                .participants(participantIds)
                .teamId(teamId)
                .build();
    }

    private ReviewResponseDto convertToReviewResponse(Review review) {
        if (review.getUsers() == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Long teamId = (review.getUsers().getTeam() != null) ? review.getUsers().getTeam().getId() : null;

        if (review.getDirectReview() != null) {
            return convertToDirectReviewDTO(review.getDirectReview(), teamId);
        }

        if (review.getRecommendationReview() != null) {
            RecommendationReview recommendationReview = review.getRecommendationReview();

            if (recommendationReview.getRecommendation() == null) {
                throw new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND);
            }

            Recommendation recommendation = recommendationReview.getRecommendation();

            if (recommendation.getFood() == null) {
                throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
            }

            Food food = recommendation.getFood();
            String foodCategory = (food.getCategory() != null) ? food.getCategory().name() : "기타";
            String foodName = food.getName();

            return convertToRecommendationReviewDTO(recommendationReview, foodName, foodCategory, recommendation, teamId);
        }


        throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
    }
}
