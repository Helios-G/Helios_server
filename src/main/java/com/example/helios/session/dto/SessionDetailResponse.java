package com.example.helios.session.dto;

import java.time.LocalDateTime;
import com.example.helios.session.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionDetailResponse {
    private Long sessionId;
    private String title;
    private String description;
    private String status;
    private int participantCount;
    private int maxParticipants;
    private String dataFormat;
    private String labelClassList;
    private int labelClassCount;
    private LocalDateTime createdAt;
    private LocalDateTime sessionEndAt;

    public static SessionDetailResponse from(Session session) {
        return new SessionDetailResponse(
            session.getSessionId(),
            session.getTitle(),
            session.getDescription(),
            mapStatus(session.getStatus()),
            session.getParticipantCount() != null ? session.getParticipantCount() : 0,
            session.getMaxParticipants() != null ? session.getMaxParticipants() : 0,
            session.getDataFormat(),
            session.getLabelClassList(),
            session.getLabelClassCount() != null ? session.getLabelClassCount() : 0,
            session.getCreatedAt(),
            session.getSessionEndAt()
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