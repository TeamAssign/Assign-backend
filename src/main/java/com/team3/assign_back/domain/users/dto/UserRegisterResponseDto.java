package com.team3.assign_back.domain.users.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserRegisterResponseDto {
    private Long id;
    private String userName;
    private String teamName;
    private String profileImageUrl;
    private BigDecimal spicy;
    private BigDecimal salty;
    private BigDecimal sweet;
    private String pros;
    private String cons;
}