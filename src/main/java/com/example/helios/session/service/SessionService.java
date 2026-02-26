package com.example.helios.session.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.helios.learning.service.LearningService;
import com.example.helios.member.entity.Hospital;
import com.example.helios.member.repository.HospitalRepository;
import com.example.helios.session.dto.SessionCreateRequest;
import com.example.helios.session.dto.SessionJoinRequest;
import com.example.helios.session.dto.SessionJoinResponse;
import com.example.helios.session.dto.SessionListResponse;
import com.example.helios.session.entity.Session;
import com.example.helios.session.entity.SessionParticipant;
import com.example.helios.session.repository.SessionParticipantRepository;
import com.example.helios.session.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final HospitalRepository hospitalRepository;
    private final SessionParticipantRepository participantRepository;
    private final LearningService learningService;

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

        session.setStatus(0); // 0: OPEN (모집 중)
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
        // 이 부분도 필요하다면 Long 타입 불일치 확인이 필요할 수 있습니다.
        List<Session> sessions =
            sessionRepository.findMySessions(hospitalId);

        return sessions.stream()
            .map(session -> {
                int currentCount =
                    participantRepository.countBySession_SessionId(session.getSessionId());

                return SessionListResponse.from(session, currentCount);
            })
            .toList();
    }

    // 세션 참여 및 자동 학습 트리거
    @Transactional
    public SessionJoinResponse joinSession(
            Long sessionId,
            SessionJoinRequest request
    ) {
        // 1. 세션 존재 검증
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));

        // 2. 병원 존재 검증
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
            .orElseThrow(() -> new IllegalArgumentException("병원이 존재하지 않습니다."));

        // 3. 이미 참여했는지 검증
        boolean alreadyJoined =
            participantRepository.existsBySession_SessionIdAndHospital_HospitalId(
                sessionId,
                request.getHospitalId()
            );

        if (alreadyJoined) {
            throw new IllegalStateException("이미 참여 신청한 세션입니다.");
        }

        // 4. 현재 참여 인원 조회
        int currentCount =
            participantRepository.countBySession_SessionId(sessionId);

        // 5. 정원 초과 검증
        if (currentCount >= session.getMaxParticipants()) {
            throw new IllegalStateException("이미 정원이 가득 찼습니다.");
        }

        // 6. 참여 정보 저장
        participantRepository.save(
            new SessionParticipant(hospital, session)
        );

        int updatedCount = currentCount + 1;

        // 7. 정원 도달 시 자동 학습 시작 (핵심 변경 로직)
        String responseStatus = "WAITING";
        
        if (updatedCount == session.getMaxParticipants()) {
            session.setStatus(2); // 2: PROGRESS (학습 진행 중)
            responseStatus = "PROGRESS";
            
            // Flower 서버에 학습 시작 요청 전송
            learningService.sendStartRequestToFlower(session);
        } else {
            session.setStatus(1); // 1: READY (모집 중이나 참여자 있음)
        }

        // 8. 응답 반환
        return new SessionJoinResponse(
            sessionId,
            updatedCount,
            session.getMaxParticipants(),
            responseStatus
        );
    }
}