package com.helios.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String name;     // "아산"
    private String email;    // "hospital@gmail.com"
    private String password; // "password123"
}