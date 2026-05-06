// service/S3Service.java
package com.example.helios.modelmgmt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String MODEL_PREFIX = "models/";

    /**
     * PUT용 Presigned URL (Flower 서버 → S3 업로드)
     */
    public String generateUploadPresignedUrl(String s3Key, String contentType, int expiryMinutes) {
        Date expiration = toDate(LocalDateTime.now().plusMinutes(expiryMinutes));

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, MODEL_PREFIX + s3Key)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        request.setContentType(contentType);

        return amazonS3.generatePresignedUrl(request).toString();
    }

    /**
     * GET용 Presigned URL (사용자 다운로드)
     */
    public String generateDownloadPresignedUrl(String s3Key, int expiryMinutes) {
        Date expiration = toDate(LocalDateTime.now().plusMinutes(expiryMinutes));

        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, MODEL_PREFIX + s3Key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(request).toString();
    }

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}