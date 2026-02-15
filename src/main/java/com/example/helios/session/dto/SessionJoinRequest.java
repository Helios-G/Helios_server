package com.example.helios.session.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionJoinRequest {
    private Long hospitalId;
    private String labelingToken;
}
