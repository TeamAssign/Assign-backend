package com.team3.assign_back.domain.tastePreference.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TastePreferenceUpdateRequestDTO {
    private BigDecimal spicy;
    private BigDecimal salty;
    private BigDecimal sweet;
    private String pros;
    private String cons;
}
