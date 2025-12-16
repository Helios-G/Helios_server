package com.example.helios.modelmgmt.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_work_code")
@Getter
@Setter
public class WorkCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workCodeId;

    @Column(length = 45, nullable = false)
    private String workCodeName;
}
