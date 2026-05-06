package com.helios.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.helios.session.entity.SessionParticipant;

public interface SessionParticipantRepository
        extends JpaRepository<SessionParticipant, Long> {

    int countBySession_SessionId(Long sessionId);

    boolean existsBySession_SessionIdAndUser_UserId(
        Long sessionId, Long userId
    );

    // ID로 참여자 전체 리스트 조회
    List<SessionParticipant> findAllBySession_SessionId(Long sessionId);
}
