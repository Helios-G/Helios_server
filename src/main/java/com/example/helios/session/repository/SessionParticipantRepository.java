package com.example.helios.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.helios.session.entity.SessionParticipant;

public interface SessionParticipantRepository
        extends JpaRepository<SessionParticipant, Long> {

    int countBySession_SessionId(Long sessionId);

    boolean existsBySession_SessionIdAndHospital_HospitalId(
        Long sessionId, Long hospitalId
    );

    // ID로 참여자 전체 리스트 조회
    List<SessionParticipant> findAllBySession_SessionId(Long sessionId);
}
