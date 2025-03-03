package com.team3.assign_back.global.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
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
        SOUTHEAST_ASIAN("아시안"),
        FAST_FOOD("패스트푸드"),
        OTHERS("기타");

        @JsonValue
        private final String koreanName;

        @JsonCreator
        public static FoodCategory from(@JsonProperty("category") String value) {
            for (FoodCategory category : values()) {
                if (category.koreanName.equals(value)) {
                    return category;
                }
            }
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum FoodType{
        SOLO("혼밥"),
        GROUP("그룹"),
        COMPANYDINNER("회식");

        @JsonValue
        private final String koreanName;

        @JsonCreator
        public static FoodType from(@JsonProperty("type") String value) {
            for (FoodType type : values()) {
                if (type.koreanName.equals(value)) {
                    return type;
                }
            }
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
    }
}