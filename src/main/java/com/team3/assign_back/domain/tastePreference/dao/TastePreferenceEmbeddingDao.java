package com.team3.assign_back.domain.tastePreference.dao;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TastePreferenceEmbeddingDao {

    private long id;
    private float[] embedding;
    private float learningRate;

}
