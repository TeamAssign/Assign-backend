package com.team3.assign_back.domain.file.service;

import com.team3.assign_back.domain.file.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public List<FileDto> generateUploadPresignedUrls(List<String> filenames) {
        return filenames.stream()
                .map(this::generatePreSignedUrl)
                .collect(Collectors.toList());
    }

    private FileDto generatePreSignedUrl(String filename) {
        String filePath = "uploads/" + UUID.randomUUID() + "_" + filename;

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(req -> req
                        .bucket(bucket)
                        .key(filePath)
                        .contentType("image/png")
                )
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return new FileDto(presignedRequest.url().toString(), filePath);
    }
}

