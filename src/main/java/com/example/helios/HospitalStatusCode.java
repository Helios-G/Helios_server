package com.mysite.sbb;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_hospital_status_code")
public class HospitalStatusCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_status_code_id")
    private Long hospitalStatusCodeId;

    @Column(name = "hospital_status_code_name", length = 45)
    private String hospitalStatusCodeName;

    protected HospitalStatusCode() {}

    // getter/setter ...
    public void setHospitalStatusCodeId(Long id) {
        this.hospitalStatusCodeId = id;
    }
}