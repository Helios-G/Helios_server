package com.mysite.sbb;

import jakarta.persistence.*;

@Entity
@Table(name = "model_information")
public class ModelInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_information_id")
    private Long modelInformationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_version_id")
    private ModelVersion modelVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_code_id")
    private WorkCode workCode;
    
    @Column(length = 1000)
    private Integer participation;

    @Column(name = "model_architecture", length = 45)
    private String modelArchitecture;

    @Column(length = 45)
    private String round;

    @Column(length = 45)
    private String accuracy;

    @Column(length = 45)
    private String loss;

    @Column(name = "work_code", columnDefinition = "INT")
    private Integer workCodeValue;  // ERD에 있는 INT(2) 컬럼

    protected ModelInformation() {}

    // getter/setter ...
    public void setModelInformationId(Long id) { 
    	this.modelInformationId = id; 
    	}
}