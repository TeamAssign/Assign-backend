package com.team3.assign_back.domain.users.dto;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private Long teamId;
    private String teamName;
    private String profileImageUrl;
}