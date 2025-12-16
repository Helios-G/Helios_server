package com.example.helios.learning.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "participation", length = 45)
    private String participation;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "data_format", length = 45)
    private String dataFormat;

    @Column(name = "label_class_list", length = 45)
    private String labelClassList;

    @Column(name = "labelclass_count")
    private Integer labelClassCount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_by", length = 45)
    private String createdBy;

    @Column(name = "created_at", length = 45)
    private String createdAt;

    @Column(name = "session_end_at", length = 45)
    private String sessionEndAt;
}
