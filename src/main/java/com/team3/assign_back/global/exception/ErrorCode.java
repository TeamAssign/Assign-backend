package com.team3.assign_back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "서버 내부 오류입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST,  "입력값이 유효하지 않습니다.");
    ;
    private final HttpStatus status;

    private final String message;
}
