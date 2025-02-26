package com.team3.assign_back.domain.recommandation.controller;

import com.team3.assign_back.domain.recommandation.dto.KakaoImageResponse;
import com.team3.assign_back.domain.recommandation.dto.KakaoPlaceResponse;
import com.team3.assign_back.domain.recommandation.dto.PlaceResponseDto;
import com.team3.assign_back.domain.recommandation.service.RecommendationService;
import com.team3.assign_back.domain.recommandation.util.KakaoApiService;
import com.team3.assign_back.global.common.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@Slf4j
public class TestController2 {
    private final KakaoApiService kakaoApiService;
    private final RecommendationService recommendationService;

    @GetMapping("/store")
    public ResponseEntity<List<KakaoPlaceResponse.Document>> searchPlaces(@RequestParam(name = "keyword") String keyword) {
        List<KakaoPlaceResponse.Document> results = kakaoApiService.findPlaces(keyword);
        //kakaoApiService.findPlaces(keyword);
        log.info("카카오 api메서드호출 :{}", keyword);
        return ResponseEntity.ok(results);
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/image")
    public ResponseEntity<Void> searchImage(@RequestParam(name = "keyword") String keyword) {
        //KakaoImageResponse.Document results = kakaoApiService.searchImage(keyword);
        kakaoApiService.searchImage(keyword);
        log.info("카카오 api메서드호출 :{}", keyword);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        //return ResponseEntity.ok(results);
    }
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<PlaceResponseDto>>> search(@RequestParam(name = "keyword") String keyword) {
        List<PlaceResponseDto> results = recommendationService.search(keyword);
        log.info("카카오 api메서드호출 :{}", keyword);
        return ApiResponseDto.from(HttpStatus.OK,"맛집 추천 성공",results);
    }

    @GetMapping("/v2")
    public ResponseEntity<ApiResponseDto<List<PlaceResponseDto>>> search2(@RequestParam(name = "keyword") String keyword) {
        List<PlaceResponseDto> results = recommendationService.search2(keyword);
        log.info("카카오 api메서드호출 :{}", keyword);
        return ApiResponseDto.from(HttpStatus.OK,"맛집 추천 성공",results);
    }
}
