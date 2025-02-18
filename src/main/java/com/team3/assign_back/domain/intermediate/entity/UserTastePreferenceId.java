package com.team3.assign_back.domain.intermediate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserTastePreferenceId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "taste_preference_id")
    private Long tastePreferenceId;
}