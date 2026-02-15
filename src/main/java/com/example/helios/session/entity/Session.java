package com.example.helios.session.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    // 세션에 어떤 병원들이 참여했는지 조회용
    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    private List<SessionParticipant> hospitalSessions = new ArrayList<>();

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "participation", length = 45)
    private String participation;

    @Column(name = "max_participants")
    private Integer maxParticipants;

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

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }   

    @Column(name = "session_end_at", length = 45)
    private LocalDateTime sessionEndAt;
}
