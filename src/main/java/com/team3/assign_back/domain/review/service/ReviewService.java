package com.team3.assign_back.domain.review.service;

import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.repository.FoodRepository;
import com.team3.assign_back.domain.image.service.ImageService;
import com.team3.assign_back.domain.intermediate.entity.Participant;
import com.team3.assign_back.domain.intermediate.repository.ParticipantRepository;
import com.team3.assign_back.domain.recommendation.entity.Recommendation;
import com.team3.assign_back.domain.recommendation.repository.RecommendationRepository;
import com.team3.assign_back.domain.intermediate.dto.ParticipantsDto;
import com.team3.assign_back.domain.review.dto.ReviewRequestDto;
import com.team3.assign_back.domain.review.dto.ReviewResponseDto;
import com.team3.assign_back.domain.review.entity.DirectReview;
import com.team3.assign_back.domain.review.entity.RecommendationReview;
import com.team3.assign_back.domain.review.entity.Review;
import com.team3.assign_back.domain.review.repository.DirectReviewRepository;
import com.team3.assign_back.domain.review.repository.RecommendationReviewRepository;
import com.team3.assign_back.domain.review.repository.ReviewRepository;
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
    private final ImageService imageService;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final FoodRepository foodRepository;
    private final RecommendationRepository recommendationRepository;
    private final ReviewRepository reviewRepository;
    private final DirectReviewRepository directReviewRepository;
    private final RecommendationReviewRepository recommendationReviewRepository;

    @Transactional
    public ReviewResponseDto createReview(Long userId, ReviewRequestDto reviewRequestDto) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewRepository.save(
                Review.builder()
                        .users(users)
                        .build()
        );

        String imageUrl = imageService.getImageUrl(reviewRequestDto.getImgurl()).imageUrl();

        return (reviewRequestDto.getRecommendationId() != null) ?
                createRecommendationReview(reviewRequestDto, review,imageUrl) :
                createDirectReview(reviewRequestDto, review, imageUrl);
    }

    private ReviewResponseDto createDirectReview(ReviewRequestDto reviewRequestDto, Review review, String imageUrl) {
        Food food = foodRepository.findByName(reviewRequestDto.getMenu())
                .orElseGet(() -> foodRepository.save(
                        Food.builder()
                                .name(reviewRequestDto.getMenu())
                                .category(reviewRequestDto.getCategory())
                                .build()
                ));

        DirectReview directReview = directReviewRepository.save(
                DirectReview.builder()
                        .review(review)
                        .type(reviewRequestDto.getType())
                        .food(food)
                        .comment(reviewRequestDto.getComment())
                        .star(reviewRequestDto.getStar())
                        .imgUrl(imageUrl)
                        .build()
        );

        List<Long> participants = (reviewRequestDto.getType() == FoodEnum.FoodType.COMPANYDINNER || reviewRequestDto.getType() == FoodEnum.FoodType.SOLO)
                ? new ArrayList<>()
                : (reviewRequestDto.getParticipants() != null ? reviewRequestDto.getParticipants() : new ArrayList<>());

        saveParticipants(review, participants);

        return convertToDirectReviewDTO(directReview);
    }

    private ReviewResponseDto createRecommendationReview(ReviewRequestDto reviewRequestDto, Review review, String imageUrl) {
        Recommendation recommendation = recommendationRepository.findById(reviewRequestDto.getRecommendationId())
                .orElseThrow(() -> new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND));

        RecommendationReview recommendationReview = recommendationReviewRepository.save(
                RecommendationReview.builder()
                        .review(review)
                        .recommendation(recommendation)
                        .comment(reviewRequestDto.getComment())
                        .star(reviewRequestDto.getStar())
                        .imgUrl(imageUrl)
                        .build()
        );

        List<Long> participants = (recommendation.getType() == FoodEnum.FoodType.COMPANYDINNER || recommendation.getType() == FoodEnum.FoodType.SOLO)
                ? new ArrayList<>()
                : (reviewRequestDto.getParticipants() != null ? reviewRequestDto.getParticipants() : new ArrayList<>());

        saveParticipants(review, participants);

        return convertToRecommendationReviewDTO(recommendationReview, recommendation);
    }

    private void saveParticipants(Review review, List<Long> participantIds) {
        if (participantIds == null) {
            participantIds = new ArrayList<>();
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

    private ReviewResponseDto convertToDirectReviewDTO(DirectReview directReview) {
        Review review = reviewRepository.findReviewWithParticipants(directReview.getReview().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        List<ParticipantsDto> participantsDtoList = review.getParticipants().stream()
                .map(participant -> ParticipantsDto.from(participant.getUsers()))
                .collect(Collectors.toList());

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .comment(directReview.getComment())
                .star(directReview.getStar())
                .food(directReview.getFood().getName())
                .type(directReview.getType())
                .category(directReview.getFood().getCategory())
                .key(directReview.getImgUrl())
                .participants(participantsDtoList)
                .build();
    }

    private ReviewResponseDto convertToRecommendationReviewDTO(RecommendationReview recommendationReview, Recommendation recommendation) {
        Review review = reviewRepository.findReviewWithParticipants(recommendationReview.getReview().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        List<ParticipantsDto> participantsDtoList = review.getParticipants().stream()
                .map(participant -> ParticipantsDto.from(participant.getUsers()))
                .collect(Collectors.toList());

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .recommendationId(recommendationReview.getRecommendation().getId())
                .comment(recommendationReview.getComment())
                .star(recommendationReview.getStar())
                .key(recommendationReview.getImgUrl())
                .type(recommendation.getType())
                .food(recommendation.getFood().getName())
                .category(recommendation.getFood().getCategory())
                .participants(participantsDtoList)
                .build();
    }

    private ReviewResponseDto convertToReviewResponse(Review review) {
        if (review.getUsers() == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }


        if (review.getDirectReview() != null) {
            return convertToDirectReviewDTO(review.getDirectReview());
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

            return convertToRecommendationReviewDTO(recommendationReview, recommendation);
        }

        throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
    }
}
