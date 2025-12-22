package com.example.helios.participation.domain;

import com.example.helios.member.domain.Hospital;
import com.example.helios.learning.domain.Session;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hospital_session")
@Getter
@NoArgsConstructor
public class HospitalSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_session_id")
    private Integer hospitalSessionId;

    // 병원 FK (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    // 세션 FK (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // 참여 상태
    // 1: 참여중, 0: 탈퇴, -1: 차단
    @Column(nullable = false)
    private Integer status;

    // 참여 시각
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    // 최소 생성자
    public HospitalSession(Hospital hospital, Session session) {
        this.hospital = hospital;
        this.session = session;
        this.status = 1; // 기본 참여중
        this.joinedAt = LocalDateTime.now();
    }
}
