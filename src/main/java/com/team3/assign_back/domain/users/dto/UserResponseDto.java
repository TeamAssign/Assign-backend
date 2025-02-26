package com.team3.assign_back.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String teamName;
    private String profileImageUrl;
}