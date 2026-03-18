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
    private Integer participantCount;
    private Integer maxParticipants;
    private String dataFormat;
    private String labelClassList;
    private Integer labelClassCount;
    private LocalDateTime createdAt;
    private LocalDateTime sessionEndAt;

    public static SessionDetailResponse from(Session session) {
        return new SessionDetailResponse(
            session.getSessionId(),
            session.getTitle(),
            session.getDescription(),
            mapStatus(session.getStatus()),
            session.getParticipantCount(),
            session.getMaxParticipants(),
            session.getDataFormat(),
            session.getLabelClassList(),
            session.getLabelClassCount(),
            session.getCreatedAt(),
            session.getSessionEndAt()
        );
    }

    private static String mapStatus(Integer status) {
        return switch (status) {
            case 0 -> "WAITING";
            case 1 -> "IN_PROGRESS";
            case 2 -> "CLOSED";
            default -> "UNKNOWN";
        };
    }
}