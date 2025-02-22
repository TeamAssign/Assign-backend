package com.team3.assign_back.domain.tastePreference.repository;

import com.team3.assign_back.domain.tastePreference.entity.TastePreferenceEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastePreferenceEmbeddingRepository extends JpaRepository<TastePreferenceEmbedding, Long> {
}
