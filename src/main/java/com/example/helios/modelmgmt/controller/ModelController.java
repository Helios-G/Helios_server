// controller/ModelController.java
package com.example.helios.modelmgmt.controller;

import com.example.helios.modelmgmt.dto.DownloadUrlResponseDto;
import com.example.helios.modelmgmt.dto.UploadUrlRequestDto;
import com.example.helios.modelmgmt.dto.UploadUrlResponseDto;
import com.example.helios.modelmgmt.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping("/upload-url")
    public ResponseEntity<UploadUrlResponseDto> getUploadUrl(
            @RequestBody UploadUrlRequestDto request) {
        return ResponseEntity.ok(modelService.generateUploadUrl(request));
    }

    @GetMapping("/{modelId}/download")
    public ResponseEntity<DownloadUrlResponseDto> getDownloadUrl(
            @PathVariable Long modelId) {
        return ResponseEntity.ok(modelService.generateDownloadUrl(modelId));
    }
}