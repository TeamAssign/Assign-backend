package com.team3.assign_back.domain.recommendation.controller;

import com.team3.assign_back.domain.recommendation.dto.PlaceResponseDto;

import com.team3.assign_back.domain.recommendation.service.RecommendationService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recommendation API", description = "맛집 추천 관련 API")
public class RecommendationController {
    private final RecommendationService recommendationService;

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
}
