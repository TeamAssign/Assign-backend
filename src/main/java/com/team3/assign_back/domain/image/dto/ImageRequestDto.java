package com.team3.assign_back.domain.image.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDto {
    private String filename;
    private String contentType;
    private Long contentLength;
}
