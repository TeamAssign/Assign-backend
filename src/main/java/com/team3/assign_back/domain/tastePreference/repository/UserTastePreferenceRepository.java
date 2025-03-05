package com.team3.assign_back.domain.tastePreference.repository;

import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTastePreferenceRepository extends JpaRepository<UserTastePreference, Long> {
    Optional<UserTastePreference> findByUsers_Id(Long userId);

    Optional<UserTastePreference> findByUsers(Users users);
}