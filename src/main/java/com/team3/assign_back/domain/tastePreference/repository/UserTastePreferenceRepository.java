package com.team3.assign_back.domain.tastePreference.repository;

import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.intermediate.entity.UserTastePreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTastePreferenceRepository extends JpaRepository<UserTastePreference, UserTastePreferenceId> {
}