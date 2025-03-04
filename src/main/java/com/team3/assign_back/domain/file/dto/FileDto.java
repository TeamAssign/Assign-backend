package com.team3.assign_back.domain.file.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private String presignedUrl;
    private String key;
}
