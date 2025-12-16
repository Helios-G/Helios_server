package com.example.helios.member.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Integer hospitalId;

    // ===== FK (나중에 연관관계로 확장 예정) =====
    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "hospital_status_code_id")
    private Integer hospitalStatusCodeId;

    // ===== 기본 정보 =====
    @Column(nullable = false, length = 45)
    private String name;

    @Column(name = "business_num", nullable = false, length = 45)
    private String businessNum;

    @Column(nullable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 45)
    private String password;

    // 가입일
    @Column(name = "date")
    private LocalDate date;

    // 상태값 (예: 0=비활성, 1=활성 등)
    @Column(length = 2)
    private Integer status;

    protected Hospital() {
        // JPA 기본 생성자
    }

    // 필요 최소 생성자
    public Hospital(String name, String businessNum, String email, String password) {
        this.name = name;
        this.businessNum = businessNum;
        this.email = email;
        this.password = password;
        this.date = LocalDate.now();
        this.status = 1;
    }

    // ===== Getter =====

    public Integer getHospitalId() {
        return hospitalId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public Integer getHospitalStatusCodeId() {
        return hospitalStatusCodeId;
    }

    public String getName() {
        return name;
    }

    public String getBusinessNum() {
        return businessNum;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getStatus() {
        return status;
    }
}
