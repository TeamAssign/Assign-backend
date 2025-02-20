package com.team3.assign_back.domain.users.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {
    private String name;
    private String teamName;
    private BigDecimal spicy;
    private BigDecimal salty;
    private BigDecimal sweet;
    private String pros;
    private String cons;
}