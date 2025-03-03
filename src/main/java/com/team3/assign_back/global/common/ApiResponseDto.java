package com.team3.assign_back.global.common;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
public record ApiResponseDto<T> (String message, T data, LocalDateTime timestamp){


    public static <T> ResponseEntity<ApiResponseDto<T>> from(HttpStatus status, String message, T data) {
        return ResponseEntity
                .status(status)
                .body(ApiResponseDto.<T>builder()
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}