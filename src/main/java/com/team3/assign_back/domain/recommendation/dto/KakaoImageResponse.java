package com.team3.assign_back.domain.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoImageResponse {
    private List<Document> documents;
    private Meta meta;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Document {
        @JsonProperty("thumbnail_url")
        private String thumbnailUrl;
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
