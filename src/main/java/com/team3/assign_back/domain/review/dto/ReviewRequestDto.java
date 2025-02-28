package com.team3.assign_back.domain.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long userId;
    private Long recommendationId;
    private Long teamId;
    private String imgurl;
    private String category;
    private String menu;
    private String type;
    private String comment;
    private Integer star;
    private List<Long> participants;

    private String teamName;

}
