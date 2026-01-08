package com.example.helios.session.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionCreateRequest {

    private String title;
    private String description;
    private Integer memberCount;
    private String dataFormat;
    private Integer classAmount;
    private List<String> classList;
    private String createdBy;
}
