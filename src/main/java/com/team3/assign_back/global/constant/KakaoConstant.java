package com.team3.assign_back.global.constant;

import org.springframework.stereotype.Component;

@Component
public class KakaoConstant {
    public static final String PLACE_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    public static final String IMAGE_API_URL = "https://dapi.kakao.com/v2/search/image";
    public static final String longitude = "127.0487824";
    public static final String latitude = "37.5037511";
    public static final int radius = 3000;

}
