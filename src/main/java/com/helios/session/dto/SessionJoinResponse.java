package com.helios.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionJoinResponse {
    private Long sessionId;
    private Integer currentParticipants;
    private Integer maxParticipants;
    private String status;
}
