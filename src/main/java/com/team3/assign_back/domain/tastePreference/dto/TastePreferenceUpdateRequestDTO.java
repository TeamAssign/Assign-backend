package com.team3.assign_back.domain.tastePreference.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class TastePreferenceUpdateRequestDTO {
    private BigDecimal spicy;
    private BigDecimal sweet;
    private BigDecimal salty;
    private String pros;
    private String cons;

    public TastePreferenceUpdateRequestDTO(BigDecimal spicy, BigDecimal sweet, BigDecimal salty, String pros, String cons){
        this.spicy = spicy;
        this.salty = salty;
        this.sweet = sweet;
        this.pros = pros;
        this.cons = cons;
    }
}
