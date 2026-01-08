package com.example.helios.participation.domain;

import java.time.LocalDateTime;

import com.example.helios.member.domain.Hospital;
import com.example.helios.session.domain.Session;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital_session")
@Getter
@NoArgsConstructor
public class SessionParticipant {

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
    public SessionParticipant(Hospital hospital, Session session) {
        this.hospital = hospital;
        this.session = session;
        this.status = 1; // 기본 참여중
        this.joinedAt = LocalDateTime.now();
    }
}
