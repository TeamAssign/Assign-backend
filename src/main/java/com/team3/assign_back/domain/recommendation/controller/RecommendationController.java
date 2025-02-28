package com.team3.assign_back.domain.recommendation.controller;

import com.team3.assign_back.domain.recommendation.dto.PlaceResponseDto;

import com.team3.assign_back.domain.recommendation.dto.RecommendationRequestDto;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.domain.recommendation.service.RecommendationService;
import com.team3.assign_back.domain.users.dto.CustomUserDetails;
import com.team3.assign_back.domain.users.service.UserService;
import com.team3.assign_back.global.common.ApiResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recommendation API", description = "맛집 추천 관련 API")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @Operation(
            summary = "메뉴 기반 맛집 추천",
            description = "사용자가 입력한 메뉴를 기반으로 관련된 맛집 리스트를 추천합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "맛집 추천 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceResponseDto.class))
    )
    @Parameter(name = "menu", description = "추천을 받을 메뉴명", required = true, example = "짜장면")
    @GetMapping("/menu")
    public ResponseEntity<ApiResponseDto<List<PlaceResponseDto>>> search(@RequestParam(name = "menu") String menu) {
        List<PlaceResponseDto> results = recommendationService.search(menu);

        return ApiResponseDto.from(HttpStatus.OK,"맛집 추천 성공",results);
    }





    @Operation(
            summary = "메뉴 추천",
            description = "사용자가 입력한 카테고리, 타입, 인원들을 기반으로 음식 메뉴를 추천합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "음식 추천 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendationResponseDto.class))
    )
    @GetMapping("/category/{category}/type/{type}")
    public ResponseEntity<ApiResponseDto<RecommendationResponseDto>> getRecommendation(@AuthenticationPrincipal Jwt jwt, @PathVariable("category")FoodEnum.FoodCategory category, @PathVariable("type")FoodEnum.FoodType type, @RequestParam(name = "participantIds", required = false) Set<Long> participantIds) {

        String vendorId = jwt.getSubject();
        Long userId = userService.getUserIdByVendorId(vendorId);

        return ApiResponseDto.from(HttpStatus.OK,"메뉴 추천 전달",recommendationService.getRecommendation(userId, category, type, participantIds));
    }



    @Operation(
            summary = "메뉴 추천 수락",
            description = "사용자가 추천받은 음식 메뉴 추천을 수락합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "메뉴 추천 수락 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @PostMapping("/menu")
    public ResponseEntity<ApiResponseDto<Void>> acceptRecommendation(@AuthenticationPrincipal Jwt jwt, @RequestBody RecommendationRequestDto recommendationRequestDto) {

        String vendorId = jwt.getSubject();
        Long userId = userService.getUserIdByVendorId(vendorId);
        recommendationService.acceptRecommendation(userId, recommendationRequestDto);

        return ApiResponseDto.from(HttpStatus.OK,"메뉴 추천 성공", null);
    }



}
