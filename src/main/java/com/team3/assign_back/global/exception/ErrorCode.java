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

    // 팀 관련 에러
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    TASTE_PREFERENCE_NOT_FOUND(HttpStatus.NOT_FOUND, "팀의 맛 선호도를 찾을 수 없습니다."),
    TEAM_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 리뷰를 찾을 수 없습니다."),
    INVALID_TEAM_SELECTION(HttpStatus.BAD_REQUEST, "본인이 속한 팀만 선택할 수 있습니다."),

    // 통계 관련 에러
    USER_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 최신 통계를 찾을 수 없습니다."),
    TEAM_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "팀의 최신 통계를 찾을 수 없습니다."),
    COMPANY_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "전사의 최신 통계를 찾을 수 없습니다."),
    BATCH_JOB_STATISTIC_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,  "통계 배치 작업 수행 중 오류가 발생했습니다."),

    // 후기 관련 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "후기를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 후기를 작성하셨습니다."),

    //추천 관련 에러
    EMPTY_PARTICIPANTS(HttpStatus.BAD_REQUEST, "그룹인 경우 본인을 제외한 참가자를 반드시 추가해야 합니다."),
    RECOMMENDATION_EXHAUSTED(HttpStatus.NOT_FOUND, "더 이상 추천을 받을 수 없습니다."),
    KAKAO_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴에 대한 맛집 리스트를 찾을 수 없습니다."),
    KAKAO_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"음식점에 관한 이미지를 찾을 수 업습니다."),
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST,"유효하지 않은 메뉴명입니다." ),

    //음식 관련 에러
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "음식 정보를 찾을 수 없습니다."),
    INVALID_FOOD_NAME(HttpStatus.BAD_REQUEST, "추천으로 받지 않은 메뉴명입니다."),
    RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "받은 추천이 존재하지 않습니다.");


    private final HttpStatus status;
    private final String message;
}