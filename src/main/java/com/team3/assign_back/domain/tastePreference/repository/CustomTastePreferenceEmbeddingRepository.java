package com.team3.assign_back.domain.tastePreference.repository;


import com.team3.assign_back.domain.tastePreference.dao.TastePreferenceEmbeddingDao;

import java.util.List;

public interface CustomTastePreferenceEmbeddingRepository {

    List<TastePreferenceEmbeddingDao> findLikeEmbeddingAndRateByUserIds(List<Long> participants);
    List<TastePreferenceEmbeddingDao> findDislikeEmbeddingAndRateByUserIds(List<Long> participants);

    List<TastePreferenceEmbeddingDao> findLikeEmbeddingAndRateForTeam(Long userId);
    List<TastePreferenceEmbeddingDao> findDislikeEmbeddingAndRateForTeam(Long userId);

    void saveLikeEmbeddingAndRate(List<TastePreferenceEmbeddingDao> tastePreferenceEmbeddingDaos);
    void saveDislikeEmbeddingAndRate(List<TastePreferenceEmbeddingDao> tastePreferenceEmbeddingDaos);

}