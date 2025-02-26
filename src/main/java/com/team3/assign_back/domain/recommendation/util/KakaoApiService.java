package com.team3.assign_back.domain.recommendation.util;

import com.team3.assign_back.domain.recommendation.dto.KakaoImageResponse;
import com.team3.assign_back.domain.recommendation.dto.KakaoPlaceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoApiService {

    private final RestTemplate restTemplate;
    private final String PLACE_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private final String IMAGE_API_URL = "https://dapi.kakao.com/v2/search/image";
    private final String API_KEY = "a05c7254310059c69f22e8b6fb8cb762";
    private final String longitude = "127.0487824";
    private final String latitude = "37.5037511";
    private final int radius = 3000;

    public List<KakaoPlaceResponse.Document> findPlaces(String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACE_API_URL)
                .queryParam("query", keyword + " 맛집")
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", radius)
                .queryParam("sort", "accuracy")
                .queryParam("category_group_code", "FD6")
                .queryParam("size", 15);

        String url = builder.build().toUriString();

        ResponseEntity<KakaoPlaceResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoPlaceResponse.class);

        List<KakaoPlaceResponse.Document> placeList = Objects.requireNonNull(response.getBody()).getDocuments();

        return placeList;
    }
      public String searchImage(String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(IMAGE_API_URL)
                .queryParam("query", keyword)
                .queryParam("size", 3)
                .queryParam("sort","accuracy");
          String url = builder.build().toUriString();

        ResponseEntity<KakaoImageResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoImageResponse.class);


        List<KakaoImageResponse.Document> image = Objects.requireNonNull(response.getBody()).getDocuments();

        return image.isEmpty()? "이미지 정보 없음" : image.get(0).getThumbnailUrl();
    }
}
