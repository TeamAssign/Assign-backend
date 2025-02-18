package com.team3.assign_back.domain.tastePreference.entity;

import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taste_preference")
public class TastePreference extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DecimalMin("1.00")
    @DecimalMax("5.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal spicy;

    @Column(nullable = false)
    @DecimalMin("1.00")
    @DecimalMax("5.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal salty;

    @Column(nullable = false)
    @DecimalMin("1.00")
    @DecimalMax("5.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal sweet;

    @Builder.Default
    @OneToMany(mappedBy = "tastePreference")
    private Set<UserTastePreference> userTastePreferences = new HashSet<>();
}