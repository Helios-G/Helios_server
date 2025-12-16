package com.example.helios.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_hospital_status_code")
@Getter
@Setter
@NoArgsConstructor
public class HospitalStatusCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_status_code_id")
    private Integer hospitalStatusCodeId;

    @Column(name = "hospital_status_code_name", length = 45)
    private String hospitalStatusCodeName;
}
