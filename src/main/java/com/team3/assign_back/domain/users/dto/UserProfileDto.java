package com.team3.assign_back.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileDto {
    private String name;
    private String teamName;
    private BigDecimal spicy;
    private BigDecimal salty;
    private BigDecimal sweet;
    private String pros;
    private String cons;
    private String imgUrl;
}
