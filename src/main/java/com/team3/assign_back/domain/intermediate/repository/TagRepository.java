package com.team3.assign_back.domain.intermediate.repository;

import com.team3.assign_back.domain.intermediate.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    void deleteByUsersId(Long userId);
}
