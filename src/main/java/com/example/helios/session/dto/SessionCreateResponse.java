package com.example.helios.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionCreateResponse {

    private Integer sessionId;
    private String status;
}
