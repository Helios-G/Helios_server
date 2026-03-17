package com.example.helios.session.dto;

public record SessionResultResponse(
    Long sessionId,
    String status,
    Long finalModelId,
    Double accuracy,      // 최종 정확도
    Double loss,          // 최종 손실값
    String completedAt    // 완료 시간
) {}