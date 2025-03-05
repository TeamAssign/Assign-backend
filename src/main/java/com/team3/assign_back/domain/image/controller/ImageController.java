package com.team3.assign_back.domain.image.controller;

import com.team3.assign_back.domain.image.dto.ImageResponseDto;
import com.team3.assign_back.domain.image.dto.ImageRequestDto;
import com.team3.assign_back.domain.image.dto.ImageUrlResponse;
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
    public ResponseEntity<ImageResponseDto> getUploadPresignedUrl(@RequestBody ImageRequestDto fileRequest) {


        ImageResponseDto presignedUrl = imageService.generateUploadPreSignedUrl(fileRequest);
        return ResponseEntity.ok(presignedUrl);
    }

    @GetMapping("/url")
    public ResponseEntity<ImageUrlResponse> getImageUrl(@RequestParam(name = "key") String key){
        ImageUrlResponse imageUrlResponse = imageService.getImageUrl(key);
        return ResponseEntity.ok(imageUrlResponse);
    }
}
