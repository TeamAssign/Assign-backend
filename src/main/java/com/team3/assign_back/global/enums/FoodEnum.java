package com.team3.assign_back.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FoodEnum {

    @Getter
    @AllArgsConstructor
    public enum FoodCategory{
        KOREAN("한식"),
        CHINESE("중식"),
        JAPANESE("일식"),
        WESTERN("양식"),
        STREET_FOOD("분식"),
        SOUTHEAST_ASIAN("동남아"),
        FAST_FOOD("패스트푸드"),
        OTHERS("기타");

        private final String koreanName;
    }
}
