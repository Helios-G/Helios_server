package com.example.helios.modelmgmt.domain;

import com.example.helios.member.domain.Hospital;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "model_information")
@Getter
@Setter
public class ModelInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer modelInformationId;


    // === FK ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospitalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_version_id", nullable = false)
    private ModelVersion modelVersion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_code_id", nullable = false)
    private WorkCode workCode;

    // == 기타 컬럼 ==
    @Column(length = 45)
    private Integer participation;

    @Column(length = 45)
    private String modelArchitecture;

    @Column(length = 45)
    private String round;

    @Column(length = 45)
    private String accuracy;

    @Column(length = 45)
    private String loss;
}
