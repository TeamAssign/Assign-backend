package com.team3.assign_back.domain.recommendation.controller;

import com.team3.assign_back.domain.recommendation.dto.PlaceResponseDto;

import com.team3.assign_back.domain.recommendation.service.RecommendationService;
import com.team3.assign_back.global.common.ApiResponseDto;
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
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/menu")
    public ResponseEntity<ApiResponseDto<List<PlaceResponseDto>>> search(@RequestParam(name = "menu") String menu) {
        List<PlaceResponseDto> results = recommendationService.search(menu);

        return ApiResponseDto.from(HttpStatus.OK,"맛집 추천 성공",results);
    }
}
