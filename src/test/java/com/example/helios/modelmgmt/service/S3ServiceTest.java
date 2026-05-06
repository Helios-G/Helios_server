package com.example.helios.modelmgmt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class S3ServiceTest {

    @Autowired
    private S3Service s3Service;

    @Test
    void presignedUrl_업로드_다운로드_왕복_테스트() throws Exception {
        String testKey = "test-dummy-" + System.currentTimeMillis() + ".zip";

        // 1. Presigned Upload URL 발급
        String uploadUrl = s3Service.generateUploadPresignedUrl(testKey, "application/zip", 5);
        assertThat(uploadUrl).contains("amazonaws.com");

        // 2. 더미 zip 바이트를 직접 PUT
        byte[] dummyZip = createDummyZipBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(uploadUrl).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/zip");
        conn.getOutputStream().write(dummyZip);
        assertThat(conn.getResponseCode()).isEqualTo(200);

        // 3. Presigned Download URL로 다시 받아오기
        String downloadUrl = s3Service.generateDownloadPresignedUrl(testKey, 5);
        HttpURLConnection dlConn = (HttpURLConnection) new URL(downloadUrl).openConnection();
        assertThat(dlConn.getResponseCode()).isEqualTo(200);
    }

    private byte[] createDummyZipBytes() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            zos.putNextEntry(new ZipEntry("model_weights.json"));
            zos.write("{\"weights\": [0.1, 0.2]}".getBytes());
            zos.closeEntry();
        }
        return baos.toByteArray();
    }
}