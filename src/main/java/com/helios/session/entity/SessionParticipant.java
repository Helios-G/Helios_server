package com.helios.session.entity;

import java.time.LocalDateTime;

import com.helios.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_session")
@Getter
@NoArgsConstructor
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_session_id")
    private Integer userSessionId;

    // 병원 FK (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 세션 FK (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // 참여 상태 (1: 참여중, 0: 탈퇴, -1: 차단)
    @Column(nullable = false)
    private Integer status;

    // 참여 시각
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    // [수정된 생성자] 연관관계 편의 로직 추가
    public SessionParticipant(User user, Session session) {
        this.user = user;
        this.session = session;
        this.status = 1; // 기본 참여중
        this.joinedAt = LocalDateTime.now();

        // === 연관관계 편의 로직 ===
        // User 객체의 참여 목록에 현재 참여 정보(this)를 추가
        if (user != null) {
            user.getUserSessions().add(this);
        }

        // Session 객체의 참여 목록에 현재 참여 정보(this)를 추가
        if (session != null) {
            session.getUserSessions().add(this);
        }
    }
}