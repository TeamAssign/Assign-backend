package com.team3.assign_back.domain.users.repository;

import com.team3.assign_back.domain.users.dto.UserResponseDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserResponseDto> searchUsersByFrequency(Long userId, int page, int size);
}