package com.example.helios.modelmgmt.domain;

import com.example.helios.admin.domain.Admin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "model_version")
@Getter
@Setter
public class ModelVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer modelVersionId;

    // === FK : 관리자가 버전 관리 ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(length = 45, nullable = false)
    private String version;

    private LocalDate fromDate;

    private LocalDate toDate;
}
