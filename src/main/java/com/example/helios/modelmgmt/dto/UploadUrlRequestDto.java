// dto/UploadUrlRequestDto.java
package com.example.helios.modelmgmt.dto;

import lombok.Getter;

@Getter
public class UploadUrlRequestDto {
    private String fileName;
    private Long sessionId;
}