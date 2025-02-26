package com.team3.assign_back.domain.users.repository;

import com.team3.assign_back.domain.users.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserResponseDto> searchUsersByFrequency(Long userId, String keyword, Pageable pageable);
}