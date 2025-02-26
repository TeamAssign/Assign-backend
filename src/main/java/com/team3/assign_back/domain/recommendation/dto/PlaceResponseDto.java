package com.team3.assign_back.domain.recommendation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponseDto {
    private String name;
    private String address;
    private String distance;
    private String phone;
    private String placeUrl;
    private String imageUrl;

    public PlaceResponseDto(KakaoPlaceResponse.Document place, String imageUrl) {
        this.name = place.getPlaceName();
        this.address = place.getRoadAddressName();
        this.distance = place.getDistance();
        this.phone = place.getPhone();
        this.placeUrl = place.getPlaceUrl();
        this.imageUrl = imageUrl;
    }
}
