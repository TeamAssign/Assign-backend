package com.team3.assign_back.domain.tastePreference.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDTO {
    private BigDecimal spicy;
    private BigDecimal salty;
    private BigDecimal sweet;
    private String pros;
    private String cons;
    private String profileImageUrl;

    public TastePreferenceUpdateRequestDTO toTastePreferenceUpdateDTO() {
        return new TastePreferenceUpdateRequestDTO(spicy, salty, sweet, pros, cons);
    }

    public UserProfileImageUpdateRequestDTO toUserProfileImageUpdateDTO() {
        return new UserProfileImageUpdateRequestDTO(profileImageUrl);
    }
}

