package com.team3.assign_back.global.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PriceEnum {

    @Getter
    @AllArgsConstructor
    public enum PriceCategory {
        BELOW_10000("1만원 이하"),
        BETWEEN_10000_AND_30000("1~3만원"),
        ABOVE_30000("3만원 초과"),
        UNKNOWN("가격 정보 없음");

        @JsonValue
        private final String label;

        // 가격 범위를 Enum으로 변환하는 메서드
        public static PriceCategory fromPrice(Double price) {
            if (price == null) {
                return UNKNOWN;
            } else if (price <= 10000) {
                return BELOW_10000;
            } else if (price <= 30000) {
                return BETWEEN_10000_AND_30000;
            } else {
                return ABOVE_30000;
            }
        }

        @JsonCreator
        public static PriceCategory from(@JsonProperty("priceCategory") String value) {
            for (PriceCategory category : values()) {
                if (category.label.equals(value)) {
                    return category;
                }
            }
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
    }
}