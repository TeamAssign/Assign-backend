package com.team3.assign_back.domain.tastePreference.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TastePreferenceUpdateRequestDTO {
    private BigDecimal spicy;
    private BigDecimal sweet;
    private BigDecimal salty;
    private String pros;
    private String cons;
}
