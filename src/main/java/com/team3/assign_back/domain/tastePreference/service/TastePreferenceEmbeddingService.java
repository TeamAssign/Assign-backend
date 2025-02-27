package com.team3.assign_back.domain.tastePreference.service;


import com.team3.assign_back.domain.food.repository.TasteMetricsEmbeddingRepository;
import com.team3.assign_back.domain.tastePreference.dao.TastePreferenceEmbeddingDao;
import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.tastePreference.entity.TastePreferenceEmbedding;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceEmbeddingRepository;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.team3.assign_back.domain.tastePreference.prompt.TastePreferencePrompt.USER_PROMPT_DISLIKES;
import static com.team3.assign_back.domain.tastePreference.prompt.TastePreferencePrompt.USER_PROMPT_LIKES;
import static com.team3.assign_back.global.constant.RecommendationConstant.*;

@Service
@RequiredArgsConstructor
public class TastePreferenceEmbeddingService {

    private final TastePreferenceRepository tastePreferenceRepository;
    private final TastePreferenceEmbeddingRepository tastePreferenceEmbeddingRepository;
    private final TasteMetricsEmbeddingRepository tasteMetricsEmbeddingRepository;

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




    @Transactional
    public void updateLikeEmbedding(Long teamId, List<Long> participants, Long foodId) {

        List<TastePreferenceEmbeddingDao> tastePreferenceEmbeddingDaos;
        if(teamId == null){
            tastePreferenceEmbeddingDaos = tastePreferenceEmbeddingRepository.findLikeEmbeddingAndRateByUserIds(participants);
        } else{
            tastePreferenceEmbeddingDaos = tastePreferenceEmbeddingRepository.findLikeEmbeddingAndRateForTeam(teamId);
        }

        float[] foodEmbed = tasteMetricsEmbeddingRepository.findTextEmbeddingByTasteMetricsId(foodId);

        for(TastePreferenceEmbeddingDao tastePreferenceEmbeddingDao : tastePreferenceEmbeddingDaos){
            float learningRate = tastePreferenceEmbeddingDao.getLearningRate();
            calculateUpdatingEmbedding(tastePreferenceEmbeddingDao.getEmbedding(),foodEmbed ,tastePreferenceEmbeddingDao.getLearningRate());
            tastePreferenceEmbeddingDao.setLearningRate(Math.max(learningRate * LIKE_EMBEDDING_DECAY_FACTOR, EMBEDDING_LEARNING_RATE_MINIMUM));
        }

        tastePreferenceEmbeddingRepository.saveLikeEmbeddingAndRate(tastePreferenceEmbeddingDaos);


    }

    @Transactional
    public void updateDislikeEmbedding(Long teamId, List<Long> participants, Long foodId) {

        List<TastePreferenceEmbeddingDao> tastePreferenceEmbeddingDaos;
        if(teamId == null){
            tastePreferenceEmbeddingDaos = tastePreferenceEmbeddingRepository.findDislikeEmbeddingAndRateByUserIds(participants);
        } else{
            tastePreferenceEmbeddingDaos = tastePreferenceEmbeddingRepository.findDislikeEmbeddingAndRateForTeam(teamId);
        }

        float[] foodEmbed = tasteMetricsEmbeddingRepository.findTextEmbeddingByTasteMetricsId(foodId);

        for(TastePreferenceEmbeddingDao tastePreferenceEmbeddingDao : tastePreferenceEmbeddingDaos){
            float learningRate = tastePreferenceEmbeddingDao.getLearningRate();
            calculateUpdatingEmbedding(tastePreferenceEmbeddingDao.getEmbedding(),foodEmbed ,tastePreferenceEmbeddingDao.getLearningRate());
            tastePreferenceEmbeddingDao.setLearningRate(Math.max(learningRate * DISLIKE_EMBEDDING_DECAY_FACTOR, EMBEDDING_LEARNING_RATE_MINIMUM));
        }

        tastePreferenceEmbeddingRepository.saveDislikeEmbeddingAndRate(tastePreferenceEmbeddingDaos);


    }

    private void calculateUpdatingEmbedding(float[] updatingEmbed, float[] foodEmbed, float learningRate){
        float squareSum = 0;
        for(int i = 0; i < updatingEmbed.length; i++){
            updatingEmbed[i] += learningRate * (foodEmbed[i] - updatingEmbed[i]);
            squareSum += updatingEmbed[i] * updatingEmbed[i];
        }
        for(int i = 0; i < updatingEmbed.length; i++){
            updatingEmbed[i] /= squareSum;
        }

    }






}
