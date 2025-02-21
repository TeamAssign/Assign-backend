package com.team3.assign_back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "서버 내부 오류입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST,  "입력값이 유효하지 않습니다."),
    CUSTOM_ERROR(HttpStatus.BAD_REQUEST, "사용자 정의 오류입니다."),

    // 사용자 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 등록된 사용자입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "인증에 실패하였습니다."),
    AUTH0_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Auth0 API 호출 중 오류가 발생하였습니다."),

    // 팀 관련 에러
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}