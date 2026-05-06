// dto/DownloadUrlResponseDto.java
package com.helios.modelmgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DownloadUrlResponseDto {
    private Long modelId;
    private String version;
    private String fileName;
    private String downloadUrl;
    private int expiryMinutes;
}