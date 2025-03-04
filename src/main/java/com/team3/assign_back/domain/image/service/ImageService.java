package com.team3.assign_back.domain.image.service;

import com.team3.assign_back.domain.image.dto.ImageResponseDto;
import com.team3.assign_back.domain.image.dto.ImageRequestDto;
import com.team3.assign_back.domain.image.dto.ImageUrlResponse;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

import static com.team3.assign_back.global.config.AWSConfig.IMAGE_MAXIMUM_LENGTH;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public ImageResponseDto generateUploadPreSignedUrl(ImageRequestDto imageRequestDto) {
        if (IMAGE_MAXIMUM_LENGTH < imageRequestDto.getContentLength()) {
            throw new CustomException(ErrorCode.EXCEED_SIZE_LIMIT);
        }

        if (!imageRequestDto.getContentType().startsWith("image")) {
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

        return ImageResponseDto.builder()
                .presignedUrl(presignedRequest.url().toExternalForm())
                .key(key)
                .build();
    }

    public ImageUrlResponse getImageUrl(String key) {
        try {
            String url = s3Client.utilities()
                    .getUrl(builder -> builder
                            .bucket(bucket)
                            .key(key))
                    .toExternalForm();
            return new ImageUrlResponse(url);
        } catch (SdkException e) {
            throw new CustomException(ErrorCode.INVALID_KEYWORD);
        }
    }
}



