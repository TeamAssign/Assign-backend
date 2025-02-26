package com.team3.assign_back.domain.tastePreference.service;


import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.tastePreference.entity.TastePreferenceEmbedding;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceEmbeddingRepository;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.stereotype.Service;

import static com.team3.assign_back.domain.tastePreference.prompt.TastePreferencePrompt.USER_PROMPT_DISLIKES;
import static com.team3.assign_back.domain.tastePreference.prompt.TastePreferencePrompt.USER_PROMPT_LIKES;

@Service
@RequiredArgsConstructor
public class TastePreferenceEmbeddingService {

    private final TastePreferenceRepository tastePreferenceRepository;
    private final TastePreferenceEmbeddingRepository tastePreferenceEmbeddingRepository;

    private final OpenAiEmbeddingModel embeddingModel;

    @Transactional
    public void saveEmbedding(Long tastePreferenceId){

        TastePreference tastePreference = tastePreferenceRepository.getReferenceById(tastePreferenceId);

        String likePrompt = String.format(USER_PROMPT_LIKES,
                tastePreference.getPros(),
                tastePreference.getSpicy(),
                tastePreference.getSweet(),
                tastePreference.getSpicy());
        float[] likeEmbedVector = embeddingModel.embed(likePrompt);

        String dislikePrompt = String.format(USER_PROMPT_DISLIKES,
                tastePreference.getCons());
        float[] dislikeEmbedVector = embeddingModel.embed(dislikePrompt);


        TastePreferenceEmbedding tastePreferenceEmbedding = TastePreferenceEmbedding.builder()
                .likeEmbedding(likeEmbedVector)
                .dislikeEmbedding(dislikeEmbedVector)
                .tastePreference(tastePreference)
                .build();

        tastePreferenceEmbeddingRepository.save(tastePreferenceEmbedding);

    }
}
