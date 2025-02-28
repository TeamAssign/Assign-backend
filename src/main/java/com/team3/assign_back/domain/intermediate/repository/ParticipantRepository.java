package com.team3.assign_back.domain.intermediate.repository;

import com.team3.assign_back.domain.intermediate.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByReviewId(Long id);
}
