package com.example.helios.session.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.helios.member.domain.Hospital;
import com.example.helios.member.repository.HospitalRepository;
import com.example.helios.participation.domain.SessionParticipant;
import com.example.helios.participation.repository.SessionParticipantRepository;
import com.example.helios.session.domain.Session;
import com.example.helios.session.dto.SessionCreateRequest;
import com.example.helios.session.dto.SessionJoinRequest;
import com.example.helios.session.dto.SessionJoinResponse;
import com.example.helios.session.dto.SessionListResponse;
import com.example.helios.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final HospitalRepository hospitalRepository;
    private final SessionParticipantRepository participantRepository;

    // 세션 생성
    public Session createSession(SessionCreateRequest request) {

        Session session = new Session();

        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setMaxParticipants(
            request.getMemberCount() != null ? request.getMemberCount() : 5
        );
        session.setDataFormat(request.getDataFormat());
        session.setLabelClassCount(request.getClassAmount());
        session.setCreatedBy(request.getCreatedBy());

        session.setLabelClassList(
            request.getClassList()
                .stream()
                .collect(Collectors.joining(","))
        );

        session.setStatus(0); // WAITING (대기 상태)

        return sessionRepository.save(session);
    }


    // 상태별 세션 목록
    public List<SessionListResponse> getSessions(Integer status) {

        List<Session> sessions =
            (status == null)
                ? sessionRepository.findAll()
                : sessionRepository.findByStatus(status);

        return sessions.stream()
            .map(session -> {
                int currentCount =
                    participantRepository.countBySession_SessionId(session.getSessionId());

                return SessionListResponse.from(session, currentCount);
            })
            .toList();
    }

    // 내가 참여 중인 세션 (병원 기준)
    public List<SessionListResponse> getMySessions(Integer hospitalId) {

        List<Session> sessions =
            sessionRepository.findMySessions(hospitalId);

        return sessions.stream()
            .map(session -> {
                int currentCount =
                    participantRepository.countBySession_SessionId(
                        session.getSessionId()
                    );

                return SessionListResponse.from(session, currentCount);
            })
            .toList();
    }


    /*
    // 세션 참여
    @Transactional
    public SessionJoinResponse joinSession(
            Integer sessionId,
            SessionJoinRequest request) {

        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));

        boolean alreadyJoined =
            participantRepository
                .existsBySession_SessionIdAndHospital_HospitalId(
                    sessionId,
                    request.getHospitalId()
                );

        if (alreadyJoined) {
            throw new IllegalStateException("이미 참여 신청한 세션입니다.");
        }

        int currentCount = participantRepository.countBySession_SessionId(session);

        if (currentCount >= session.getMaxParticipants()) {
            throw new IllegalStateException("이미 정원이 가득 찼습니다.");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
            .orElseThrow(() -> new IllegalArgumentException("병원이 존재하지 않습니다."));

        participantRepository.save(
            new SessionParticipant(hospital, session)
        );

        int updatedCount = currentCount + 1;

        if (updatedCount == session.getMaxParticipants()) {
            session.setStatus(1); // READY
        }

        return new SessionJoinResponse(
            sessionId,
            updatedCount,
            session.getMaxParticipants(),
            "WAITING"
        );
    }
    */
}
