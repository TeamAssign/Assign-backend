package com.team3.assign_back.domain.intermediate.entity;

import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_taste_preference")
public class UserTastePreference extends BaseEntity {
    @EmbeddedId
    private UserTastePreferenceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tastePreferenceId")
    @JoinColumn(name = "taste_preference_id", nullable = false)
    private TastePreference tastePreference;
}