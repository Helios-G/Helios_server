package com.example.helios.member.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import com.example.helios.session.entity.SessionParticipant;
import com.example.helios.admin.entity.Admin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hospital")
@Getter // 모든 필드에 대한 Getter 자동 생성
@Setter // 모든 필드에 대한 Setter 자동 생성 (필요에 따라 사용)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 자동 생성
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long hospitalId;

    // ===== FK =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // 병원이 어떤 세션에 참여했는지 조회용
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

    // 필요 최소 생성자
    public Hospital(String name, String businessNum, String email, String password) {
        this.name = name;
        this.businessNum = businessNum;
        this.email = email;
        this.password = password;
        this.date = LocalDate.now();
        this.status = 1; // 기본 활성화 상태
    }
}