package com.team3.assign_back.domain.recommandation.service;

import com.team3.assign_back.domain.recommandation.dto.KakaoPlaceResponse;
import com.team3.assign_back.domain.recommandation.dto.PlaceResponseDto;
import com.team3.assign_back.domain.recommandation.util.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final KakaoApiService kakaoApiService;
    private final ExecutorService executorService;
    public List<PlaceResponseDto> search(String keyword) {
        long startTime = System.nanoTime();
        List<KakaoPlaceResponse.Document> places = kakaoApiService.findPlaces(keyword);
        List<PlaceResponseDto> result = places.stream()
                .map(place -> {
                    String imageUrl = kakaoApiService.searchImage(place.getPlaceName());
                    return new PlaceResponseDto(place, imageUrl);
                })
                .collect(Collectors.toList());
        long endTime = System.nanoTime();  // 종료 시간
        log.info("****동기 방식 실행 시간: {} ms", (endTime - startTime) / 1_000_000.0);
        return  result;
    }

    public List<PlaceResponseDto> search2(String keyword) {
        long startTime = System.nanoTime();
        List<KakaoPlaceResponse.Document> places = kakaoApiService.findPlaces(keyword);

        List<CompletableFuture<PlaceResponseDto>> futures = places.stream()
                .map(place -> CompletableFuture.supplyAsync(() -> {
                    String imageUrl = kakaoApiService.searchImage(place.getPlaceName());
                    return new PlaceResponseDto(place, imageUrl);
                }, executorService))
                .collect(Collectors.toList());
        List<PlaceResponseDto> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        long endTime = System.nanoTime();  // 종료 시간
        log.info("**** 비동기 방식 실행 시간: {} ms", (endTime - startTime) / 1_000_000.0);

        return result;
    }

}
