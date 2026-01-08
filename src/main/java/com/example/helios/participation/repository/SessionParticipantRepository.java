package com.example.helios.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.helios.participation.domain.SessionParticipant;
import com.example.helios.session.domain.Session;

public interface SessionParticipantRepository
        extends JpaRepository<SessionParticipant, Long> {

    int countBySession_SessionId(Integer sessionId);

    boolean existsBySession_SessionIdAndHospital_HospitalId(
        Integer sessionId, Long hospitalId
    );
}
