package com.team3.assign_back.domain.review.controller;

import com.team3.assign_back.domain.review.dto.ReviewRequestDto;
import com.team3.assign_back.domain.review.dto.ReviewResponseDto;
import com.team3.assign_back.domain.review.service.ReviewService;
import com.team3.assign_back.domain.users.service.UserService;
import com.team3.assign_back.global.common.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Review API", description = "리뷰 관련 API")
public class ReviewController {
    private final UserService userService;
    private final ReviewService reviewService;


    @Operation(
            summary = "리뷰 등록",
            description = "신규 리뷰를 등록하는 API입니다."
    )
    @ApiResponse(
            responseCode = "201",
            description = "리뷰 생성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @PostMapping("/reviews")
    public ResponseEntity<ApiResponseDto<ReviewResponseDto>> createReviews(@RequestBody ReviewRequestDto reviewRequestDto,
                                                                           @AuthenticationPrincipal Jwt jwt
                                                        ){
        String vendorId = jwt.getSubject();
        Long userId = userService.getUserIdByVendorId(vendorId);
        log.info("Received userId: {}", userId);

        ReviewResponseDto reviewResponseDto = reviewService.createReview(userId, reviewRequestDto);
        return ApiResponseDto.from(HttpStatus.CREATED, "후기가 성공적으로 등록되었습니다.", reviewResponseDto);
    }
}
