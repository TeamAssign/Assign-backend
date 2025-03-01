package com.team3.assign_back.domain.users.repository;

import com.team3.assign_back.domain.users.dto.UserSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserSearchResponseDto> searchUsersByFrequency(Long userId, String name, Pageable pageable);
}