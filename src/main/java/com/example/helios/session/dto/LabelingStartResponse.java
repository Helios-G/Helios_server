package com.example.helios.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LabelingStartResponse {
    private String labelingSessionId;
    private Long sessionId;
}
