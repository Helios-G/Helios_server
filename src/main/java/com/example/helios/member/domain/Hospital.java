package com.example.helios.member.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import com.example.helios.participation.domain.SessionParticipant;
import com.example.helios.admin.domain.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Integer hospitalId;

    // ===== FK =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    //병원이 어떤 세션에 참여했는지 조회용
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY)
    private List<SessionParticipant> hospitalSessions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_status_code_id")
    private HospitalStatusCode hospitalStatusCode;

    // ===== 기본 정보 =====
    @Column(nullable = false, length = 45)
    private String name;

    @Column(name = "business_num", nullable = false, length = 45)
    private String businessNum;

    @Column(nullable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 45)
    private String password;

    @Column(name = "date")
    private LocalDate date;

    // 상태값 (0 : 비활성, 1 : 활성, 9 : 삭제)
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

    public Admin getAdmin() {
        return admin;
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
