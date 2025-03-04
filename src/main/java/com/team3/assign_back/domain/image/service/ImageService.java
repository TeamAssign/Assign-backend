package com.team3.assign_back.domain.image.service;

import com.team3.assign_back.domain.image.dto.ImageDto;
import com.team3.assign_back.domain.image.dto.ImageRequestDto;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.awt.*;
import java.time.Duration;
import java.util.UUID;

import static com.team3.assign_back.global.config.AWSConfig.IMAGE_MAXIMUM_LENGTH;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public ImageDto generateUploadPreSignedUrl(ImageRequestDto imageRequestDto) {
        if(IMAGE_MAXIMUM_LENGTH < imageRequestDto.getContentLength()){
            throw new CustomException(ErrorCode.EXCEED_SIZE_LIMIT);
        }

        if(!imageRequestDto.getContentType().startsWith("image")){
            throw new CustomException(ErrorCode.NOT_MATCHED_TYPE);
        }

        String key = "uploads/" + UUID.randomUUID() + "_" + imageRequestDto.getFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(imageRequestDto.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return ImageDto.builder()
                .presignedUrl(presignedRequest.url().toExternalForm())
                .key(key)
                .build();
    }
}

