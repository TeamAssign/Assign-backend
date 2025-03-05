package com.team3.assign_back.domain.image.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDto {
    private String fileName;
    private String contentType;
    private Long contentLength;
}
