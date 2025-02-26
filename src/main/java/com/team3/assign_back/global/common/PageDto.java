package com.team3.assign_back.global.common;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    @Min(1)
    private Integer page = 1;

    @Min(1)
    private Integer size = 10;
}