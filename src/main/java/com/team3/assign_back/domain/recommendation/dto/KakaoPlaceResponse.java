package com.team3.assign_back.domain.recommendation.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPlaceResponse {
    private List<Document> documents;
    private Meta meta;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Document {
        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("place_url")
        private String placeUrl;

        private String phone;
        private String distance;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("is_end")
        private boolean isEnd;
    }
}