package com.team3.assign_back.domain.tastePreference.entity;

import com.team3.assign_back.domain.intermediate.entity.TeamTastePreference;
import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.tastePreference.dto.TastePreferenceUpdateRequestDTO;
import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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
    private BigDecimal sweet;

    @Column(nullable = false)
    @DecimalMin("1.00")
    @DecimalMax("5.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal salty;


    @Column(nullable = true)
    private String pros; // 호 데이터

    @Column(nullable = true)
    private String cons; // 불 데이터

    @Builder.Default
    @OneToMany(mappedBy = "tastePreference")
    private List<UserTastePreference> userTastePreferences = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "tastePreference")
    private List<TeamTastePreference> TeamTastePreferences = new ArrayList<>();

    public void updateTastePreferences(TastePreferenceUpdateRequestDTO tastePreferenceUpdateRequestDTO){
        this.spicy = tastePreferenceUpdateRequestDTO.getSpicy();
        this.salty = tastePreferenceUpdateRequestDTO.getSalty();
        this.sweet = tastePreferenceUpdateRequestDTO.getSweet();
        this.pros = tastePreferenceUpdateRequestDTO.getPros();
        this.cons = tastePreferenceUpdateRequestDTO.getCons();
    }
}