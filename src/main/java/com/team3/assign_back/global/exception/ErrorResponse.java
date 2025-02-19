package com.team3.assign_back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return new ErrorResponse(errorCode.name(), customMessage);
    }
}
