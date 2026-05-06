package com.helios.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String grantType;   // "Bearer"
    private String accessToken;
}