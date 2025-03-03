package com.team3.assign_back.domain.users.controller;

import com.team3.assign_back.domain.users.service.Auth0Service;
import com.team3.assign_back.global.common.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public/auth0")
@RequiredArgsConstructor
public class Auth0Controller {
    private final Auth0Service auth0Service;

    @GetMapping("/management-token")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> getManagementToken() {
        String token = auth0Service.getManagementApiToken();
        Map<String, String> response = new HashMap<>();
        response.put("access_token", token);
        return ApiResponseDto.from(HttpStatus.OK, "Management API 토큰입니다.", response);
    }
}