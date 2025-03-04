package com.team3.assign_back.domain.image.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String presignedUrl;
    private String key;
}
