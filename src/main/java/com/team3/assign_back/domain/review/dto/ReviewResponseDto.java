package com.team3.assign_back.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReviewResponseDto {
    private Long userId;;
    private Long reviewId;
    private String comment;
    private int star;
    private String imgurl;
    private List<Long> participants;
    private String type;
    private String category;
    private String food;
    private Long recommendationId;
    private Long teamId;
}
