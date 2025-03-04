package com.team3.assign_back.domain.file.controller;

import com.team3.assign_back.domain.file.dto.FileDto;
import com.team3.assign_back.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload-url")
    public ResponseEntity<List<FileDto>> getUploadPresignedUrls(@RequestParam("files") List<MultipartFile> files) {
        List<String> filenames = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());

        List<FileDto> presignedUrls = fileService.generateUploadPresignedUrls(filenames);
        return ResponseEntity.ok(presignedUrls);
    }
}
