package com.team3.assign_back.domain.users.controller;

import com.team3.assign_back.domain.users.dto.UserRegisterRequestDto;
import com.team3.assign_back.domain.users.dto.UserResponseDto;
import com.team3.assign_back.domain.users.dto.UserSearchResponseDto;
import com.team3.assign_back.domain.users.service.UserService;
import com.team3.assign_back.global.common.ApiResponseDto;
import com.team3.assign_back.global.common.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "유저 등록",
            description = "신규 유저를 등록하는 API입니다."
    )
    @ApiResponse(
            responseCode = "201",
            description = "유저 생성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<Void>> registerUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        String vendorId = jwt.getSubject();
        userService.registerUser(vendorId, userRegisterRequestDto);

        return ApiResponseDto.from(HttpStatus.CREATED, "유저 생성이 완료되었습니다.", null);
    }

    @Operation(
            summary = "현재 로그인한 유저 정보 조회",
            description = "현재 로그인한 유저의 정보를 반환하는 API입니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저 정보 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
    )
    @GetMapping("/userInfo")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String vendorId = jwt.getSubject();
        UserResponseDto userInfo = userService.getUserInfo(vendorId);
        return ApiResponseDto.from(HttpStatus.OK, "유저 정보 조회 성공.", userInfo);
    }

    @Operation(
            summary = "유저 검색",
            description = "이름을 기준으로 유저를 검색합니다. 페이지네이션 기능을 제공합니다. 기본값: page=1, size=10"
    )
    @ApiResponse(
            responseCode = "200",
            description = "사용자 검색 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSearchResponseDto.class))
    )
    @Parameter(name = "name", description = "검색할 유저의 이름 (빈 문자열이면 전체 검색)", example = "홍길동")
    @Parameter(name = "page", description = "페이지 번호 (기본값: 1)", example = "1")
    @Parameter(name = "size", description = "페이지 크기 (기본값: 10)", example = "10")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponseDto<UserSearchResponseDto>>> searchUsers(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        String vendorId = jwt.getSubject();
        Long userId = userService.getUserIdByVendorId(vendorId);

        Page<UserSearchResponseDto> userPage = userService.searchUsers(userId, name, page, size);
        PageResponseDto<UserSearchResponseDto> response = new PageResponseDto<>(userPage);

        return ApiResponseDto.from(HttpStatus.OK, "사용자 검색 결과입니다.", response);
    }
}