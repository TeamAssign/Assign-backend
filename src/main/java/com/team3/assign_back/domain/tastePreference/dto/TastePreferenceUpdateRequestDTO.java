package com.team3.assign_back.domain.tastePreference.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TastePreferenceUpdateRequestDTO {
    private BigDecimal spicy;
    private BigDecimal salty;
    private BigDecimal sweet;
    private String pros;
    private String cons;
}
