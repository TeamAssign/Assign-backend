package com.team3.assign_back.domain.image.controller;

import com.team3.assign_back.domain.image.dto.ImageDto;
import com.team3.assign_back.domain.image.dto.ImageRequestDto;
import com.team3.assign_back.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageDto> getUploadPresignedUrl(@RequestBody ImageRequestDto fileRequest) {


        ImageDto presignedUrl = imageService.generateUploadPreSignedUrl(fileRequest);
        return ResponseEntity.ok(presignedUrl);
    }
}
