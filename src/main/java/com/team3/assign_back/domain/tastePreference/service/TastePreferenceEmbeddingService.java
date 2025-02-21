package com.team3.assign_back.domain.tastePreference.service;


import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.tastePreference.entity.TastePreferenceEmbedding;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceEmbeddingRepository;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.stereotype.Service;

import static com.team3.assign_back.domain.tastePreference.prompt.TastePreferencePrompt.USER_PROMPT;

@Service
@RequiredArgsConstructor
public class TastePreferenceEmbeddingService {

    private final TastePreferenceRepository tastePreferenceRepository;
    private final TastePreferenceEmbeddingRepository tastePreferenceEmbeddingRepository;

    private final OpenAiEmbeddingModel embeddingModel;

    @Transactional
    public void saveUserEmbedding(Long tastePreferenceId){

        TastePreference tastePreference = tastePreferenceRepository.getReferenceById(tastePreferenceId);

//        String prompt = String.format(USER_PROMPT,
//                tastePreference.getPros(),
//                tastePreference.getCons(),
//                tastePreference.getSpicy(),
//                tastePreference.getSweet(),
//                tastePreference.getSpicy());
        String prompt = null;
        float[] embedVector = embeddingModel.embed(prompt);

        TastePreferenceEmbedding tastePreferenceEmbedding = TastePreferenceEmbedding.builder()
                .textEmbedding(embedVector)
                .tastePreference(tastePreference)
                .build();

        tastePreferenceEmbeddingRepository.save(tastePreferenceEmbedding);

    }
}
