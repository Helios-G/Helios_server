package com.example.helios.session.dto;

import com.example.helios.session.domain.Session;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionListResponse {

    private Integer sessionId;
    private String title;
    private String status;
    private int currentParticipants;
    private int maxParticipants;
    private boolean joined;

    public static SessionListResponse from(
        Session session,
        int currentParticipants
    ) {
        int maxParticipants =
                session.getMaxParticipants() != null
                        ? session.getMaxParticipants()
                        : 0;

        return new SessionListResponse(
            session.getSessionId(),
            session.getTitle(),
            mapStatus(session.getStatus()),
            currentParticipants,
            maxParticipants,
            true
        );
    }

    // 상태값 변환
    private static String mapStatus(Integer status) {
        return switch (status) {
            case 0 -> "OPEN";
            case 1 -> "READY";
            case 2 -> "CLOSED";
            default -> "UNKNOWN";
        };
    }
}
