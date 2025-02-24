package com.team3.assign_back.domain.tastePreference.repository;

import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TastePreferenceRepository extends JpaRepository<TastePreference, Long> {
}