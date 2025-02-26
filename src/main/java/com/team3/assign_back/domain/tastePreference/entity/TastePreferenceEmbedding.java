package com.team3.assign_back.domain.tastePreference.entity;

import com.team3.assign_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JoinColumn(name = "taste_preference_id")
    private TastePreference tastePreference;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 256)
    @Column(name = "like_embedding")
    private float[] likeEmbedding;


    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 256)
    @Column(name = "dislike_embedding")
    private float[] dislikeEmbedding;


}