package com.example.helios.modelmgmt.entity;

import java.time.LocalDateTime;

import com.example.helios.session.entity.Session;
import com.example.helios.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "model_information")
@Getter
@Setter
@NoArgsConstructor
public class ModelInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_information_id")
    private Long modelInformationId;

    @Column(name = "file_name", length = 100)
    private String fileName;  // "20260331-Round5-v1.zip"

    // === FK 연관 관계 ===

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_version_id", nullable = false)
    private ModelVersion modelVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_code_id", nullable = false)
    private WorkCode workCode;

    // === 기타 컬럼 ===

    private Integer participation;

    @Column(name = "model_architecture", length = 45)
    private String modelArchitecture;

    private Integer round; 

    private Double accuracy; 

    private Double loss; 

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

