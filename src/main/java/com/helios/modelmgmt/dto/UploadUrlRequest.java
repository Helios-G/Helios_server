package com.helios.modelmgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UploadUrlRequest {

    @NotBlank(message = "파일명을 입력해주세요.")
    private String fileName;

    @NotNull(message = "세션 ID를 입력해주세요.")
    private Long sessionId;
}