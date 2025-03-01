package com.team3.assign_back.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSearchResponseDto {
    private Long id;
    private String name;
    private String teamName;
    private String profileImageUrl;
}