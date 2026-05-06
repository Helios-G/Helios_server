package com.helios.modelmgmt.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.helios.modelmgmt.dto.DownloadUrlResponse;
import com.helios.modelmgmt.dto.UploadUrlRequest;
import com.helios.modelmgmt.dto.UploadUrlResponse;
import com.helios.modelmgmt.entity.ModelInformation;
import com.helios.modelmgmt.exception.ModelNotFoundException;
import com.helios.modelmgmt.repository.ModelInformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final S3Service s3Service;
    private final ModelInformationRepository modelInformationRepository;

    private static final int UPLOAD_EXPIRY_MINUTES = 10;
    private static final int DOWNLOAD_EXPIRY_MINUTES = 10;

    // POST /models/upload-url
    public UploadUrlResponse generateUploadUrl(UploadUrlRequest request) {
        String s3Key = request.getFileName();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(UPLOAD_EXPIRY_MINUTES);

        String presignedUrl = s3Service.generateUploadPresignedUrl(
                s3Key, "application/zip", UPLOAD_EXPIRY_MINUTES
        );

        // sessionId로 ModelInformation 조회 후 fileName 저장
        ModelInformation model = modelInformationRepository
                .findBySession_SessionId(request.getSessionId())
                .orElseThrow(() -> new ModelNotFoundException(request.getSessionId()));

        model.setFileName(s3Key);
        modelInformationRepository.save(model);

        return new UploadUrlResponse(presignedUrl, model.getModelInformationId(), expiration);
    }

    // GET /models/{modelId}/download
    public DownloadUrlResponse generateDownloadUrl(Long modelId) {
        ModelInformation model = modelInformationRepository.findById(modelId)
                .orElseThrow(() -> new ModelNotFoundException(modelId));

        String fileName = model.getFileName();
        String version = model.getModelVersion().getVersion();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(DOWNLOAD_EXPIRY_MINUTES);

        String downloadUrl = s3Service.generateDownloadPresignedUrl(fileName, DOWNLOAD_EXPIRY_MINUTES);

        return new DownloadUrlResponse(modelId, version, downloadUrl, expiration);
    }
}