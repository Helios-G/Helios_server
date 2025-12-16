package com.example.helios.modelmgmt.domain;

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

    // 나중에 Admin 엔티티와 연관
    private Integer adminId;

    @Column(length = 45, nullable = false)
    private String version;

    private LocalDate fromDate;

    private LocalDate toDate;
}
