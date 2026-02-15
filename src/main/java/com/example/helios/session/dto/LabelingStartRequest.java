package com.example.helios.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LabelingStartRequest {
    private Integer sessionId;
    private String labelingType; // AUTO / MANUAL
}

