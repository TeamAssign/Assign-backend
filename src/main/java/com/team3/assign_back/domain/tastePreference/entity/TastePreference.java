package com.team3.assign_back.domain.tastePreference.entity;

import com.team3.assign_back.domain.intermediate.entity.TeamTastePreference;
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

    @Column(nullable = true)
    private String pros; // 호 데이터

    @Column(nullable = true)
    private String cons; // 불 데이터

    @Builder.Default
    @OneToMany(mappedBy = "tastePreference")
    private Set<UserTastePreference> userTastePreferences = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "tastePreference")
    private Set<TeamTastePreference> TeamTastePreferences = new HashSet<>();

    public void updateTastePreferences(TastePreference tastePreference){
        this.spicy = tastePreference.getSpicy();
        this.salty = tastePreference.getSalty();
        this.sweet = tastePreference.getSweet();
        this.pros = tastePreference.getPros();
        this.cons = tastePreference.getCons();
    }
}