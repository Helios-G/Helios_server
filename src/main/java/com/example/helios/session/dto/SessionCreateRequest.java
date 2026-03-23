package com.example.helios.session.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SessionCreateRequest {

    private String title;
    private String description;
    private Integer maxParticipants;
    private String dataFormat;
    private Integer labelClassCount;
    private List<String> labelClassList;
    private Long createdBy;
}
