package com.team3.assign_back.domain.tastePreference.entity;

import com.team3.assign_back.global.common.BaseEntity;
import com.team3.assign_back.global.common.FloatArrayToVectorConverter;
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
@Table(name = "taste_preference_embedding")
public class TastePreferenceEmbedding extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "taste_metrics_id")
    private TastePreference tastePreference;


    @Convert(converter = FloatArrayToVectorConverter.class)
    @Column(name = "text_embedding", columnDefinition = "vector(256)")
    private float[] textEmbedding;


    @Convert(converter = FloatArrayToVectorConverter.class)
    @Column(name = "preference_embedding", columnDefinition = "vector(3)")
    private float[] preferenceEmbedding;


}