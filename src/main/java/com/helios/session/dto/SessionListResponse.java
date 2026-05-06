package com.helios.session.dto;

import com.helios.session.entity.Session;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionListResponse {

    private Long sessionId;
    private String title;
    private String status;
    private int currentParticipants;
    private int maxParticipants;
    private boolean joined;

    public static SessionListResponse from(
        Session session,
        int currentParticipants,
        boolean joined          // 추가
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
            joined              
        );
    }

    private static String mapStatus(Integer status) {
        if (status == null) return "UNKNOWN";
        return switch (status) {
            case 0 -> "WAITING";
            case 1 -> "IN_PROGRESS";
            case 2 -> "COMPLETED";
            case 3 -> "FAILED";
            default -> "UNKNOWN";
        };
    }
}