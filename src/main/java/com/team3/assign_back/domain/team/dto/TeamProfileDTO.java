package com.team3.assign_back.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TeamProfileDTO {
    private String team;
    private BigDecimal spicy;
    private BigDecimal sweet;
    private BigDecimal salty;
    private String pros;
    private String cons;
}
