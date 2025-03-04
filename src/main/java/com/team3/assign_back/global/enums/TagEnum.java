package com.team3.assign_back.global.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagEnum {

    @Getter
    @AllArgsConstructor
    public enum MealTag {
        TRENDY("트렌디한"),
        LUXURIOUS("고급스러운"),
        CLEAN("깔끔한"),
        COMFORTABLE("편안한"),
        HEALING("힐링되는"),
        CASUAL("캐주얼한"),
        UNIQUE("유니크한"),
        PLENTIFUL("푸짐한"),
        ORGANIZED("정갈한"),
        FAMILIAR_TASTE("익숙한 맛"),
        MODERN("모던한"),
        HOMEY("정겨운"),
        DIVERSE_MENU("다양한 메뉴"),
        CLASSIC("클래식한"),
        LIGHT_MEAL("가벼운 한 끼"),
        GOOD_VALUE("가성비"),
        EXOTIC("이국적인");


        @JsonValue
        private final String koreanTag;

        @JsonCreator
        public static TagEnum.MealTag from( String value) {
            for (TagEnum.MealTag tag : values()) {
                if (tag.koreanTag.equals(value)) {
                    return tag;
                }
            }
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
    }


}
