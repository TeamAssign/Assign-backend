package com.team3.assign_back.domain.recommendation.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDto {

    private String name;
    private String imageUrl;
    private BigDecimal accuracy;

}
