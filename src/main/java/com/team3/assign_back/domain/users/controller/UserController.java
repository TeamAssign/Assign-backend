package com.team3.assign_back.domain.users.controller;

import com.team3.assign_back.domain.users.dto.UserRegisterRequestDto;
import com.team3.assign_back.domain.users.dto.UserResponseDto;
import com.team3.assign_back.domain.users.service.UserService;
import com.team3.assign_back.global.common.ApiResponseDto;
import com.team3.assign_back.global.common.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<Void>> registerUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        String vendorId = jwt.getSubject();
        userService.registerUser(vendorId, userRegisterRequestDto);

        return ApiResponseDto.from(HttpStatus.CREATED, "유저 생성이 완료되었습니다.", null);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> searchUsers(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute PageDto pageDto) {
        int page = Optional.ofNullable(pageDto.getPage()).filter(p -> p > 0).orElse(1);
        int size = Optional.ofNullable(pageDto.getSize()).filter(s -> s > 0).orElse(10);

        String vendorId = jwt.getSubject();
        Long userId = userService.getIdUserByVendorId(vendorId);

        List<UserResponseDto> users = userService.searchUsers(userId, page, size);

        return ApiResponseDto.from(HttpStatus.OK, "사용자 검색 결과입니다.", users);
    }
}