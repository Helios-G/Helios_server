package com.helios.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.helios.admin.entity.Admin;
import com.helios.session.entity.SessionParticipant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter 
@Setter 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // ===== FK =====

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SessionParticipant> userSessions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_code_id")
    private RoleCode roleCode; 

    // ===== 기본 정보 =====
    @Column(nullable = false, length = 45)
    private String name;

    // nullable = false 제거: 일반 사용자는 가입 시 사업자 번호가 없을 수 있음
    @Column(name = "business_num", length = 45) 
    private String businessNum;

    @Column(nullable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 2)
    private Integer status;

    // 기존 생성자 (인증된 사용자용 - 파라미터 4개)
    public User(String name, String businessNum, String email, String password) {
        this.name = name;
        this.businessNum = businessNum;
        this.email = email;
        this.password = password;
        this.status = 1; 
    }

    // 계정 생성일 자동 추가
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 추가된 생성자 
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.businessNum = null; // 명시적으로 null 처리
        this.status = 1;
    }
}