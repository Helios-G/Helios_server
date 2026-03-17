package com.example.helios.session.dto;

public record SessionStatusResponse(
    Long sessionId,
    String status,        // WAITING, PROGRESS, COMPLETED, FAILED
    Integer currentRound, // 현재 몇 라운드인지
    Integer totalRounds,  // 총 라운드
    Double progress,      // (currentRound / totalRounds) * 100
    String message        // 진행 상황 메시지
) {}
