// dto/UploadUrlRequestDto.java
package com.helios.modelmgmt.dto;

import lombok.Getter;

@Getter
public class UploadUrlRequestDto {
    private String fileName;
    private Long sessionId;
}