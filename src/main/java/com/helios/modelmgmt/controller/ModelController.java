package com.helios.modelmgmt.controller;

import com.helios.common.response.ApiResponse;
import com.helios.modelmgmt.dto.DownloadUrlResponse;
import com.helios.modelmgmt.dto.UploadUrlRequest;
import com.helios.modelmgmt.dto.UploadUrlResponse;
import com.helios.modelmgmt.service.ModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping("/upload-url")
    public ResponseEntity<ApiResponse<UploadUrlResponse>> getUploadUrl(
            @RequestBody @Valid UploadUrlRequest request) {
        UploadUrlResponse data = modelService.generateUploadUrl(request);
        return ResponseEntity.ok(ApiResponse.of("업로드 URL이 발급되었습니다.", data));
    }

    @GetMapping("/{modelId}/download")
    public ResponseEntity<ApiResponse<DownloadUrlResponse>> getDownloadUrl(
            @PathVariable Long modelId) {
        DownloadUrlResponse data = modelService.generateDownloadUrl(modelId);
        return ResponseEntity.ok(ApiResponse.of("다운로드 URL이 발급되었습니다.", data));
    }
}