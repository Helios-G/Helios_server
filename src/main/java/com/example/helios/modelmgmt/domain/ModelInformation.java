package com.example.helios.modelmgmt.domain;

import jakarta.persistence.*;
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

    // 나중에 Hospital 엔티티와 연관
    private Integer hospitalId;

    // 나중에 ModelVersion 엔티티와 연관
    private Integer modelVersionId;

    // 나중에 WorkCode 엔티티와 연관
    private Integer workCodeId;

    private Integer participation;

    @Column(length = 45)
    private String modelArchitecture;

    @Column(length = 45)
    private String round;

    @Column(length = 45)
    private String accuracy;

    @Column(length = 45)
    private String loss;

    @Column(length = 2)
    private Integer workCode;
}
