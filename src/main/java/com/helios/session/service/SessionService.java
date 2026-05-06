package com.helios.session.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helios.learning.service.LearningService;
import com.helios.session.dto.SessionCreateRequest;
import com.helios.session.dto.SessionDetailResponse;
import com.helios.session.dto.SessionJoinResponse;
import com.helios.session.dto.SessionListResponse;
import com.helios.session.entity.Session;
import com.helios.session.entity.SessionParticipant;
import com.helios.session.repository.SessionParticipantRepository;
import com.helios.session.repository.SessionRepository;
import com.helios.user.entity.User;
import com.helios.user.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionParticipantRepository participantRepository;
    private final LearningService learningService;

    // 세션 생성
    public Session createSession(SessionCreateRequest request) {
        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setMaxParticipants(
            request.getMaxParticipants() != null ? request.getMaxParticipants() : 5
        );
        session.setDataFormat(request.getDataFormat());
        session.setLabelClassCount(request.getLabelClassCount());
        session.setStatus(0); // WAITING
        session.setParticipantCount(0);
        session.setCreatedBy(request.getCreatedBy());

        if (request.getLabelClassList() != null) {
            session.setLabelClassList(
                request.getLabelClassList().stream().collect(Collectors.joining(","))
            );
        }

        Session savedSession = sessionRepository.save(session);

        // 생성자를 자동으로 참여자로 등록
        if (request.getCreatedBy() != null) {
            User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            participantRepository.save(new SessionParticipant(user, savedSession));
            savedSession.setParticipantCount(1); // 참여자 수 1로 업데이트
            sessionRepository.save(savedSession);
        }

        return savedSession;
    }

    // 2. 전체/상태별 세션 목록 (joined 여부 추가)
    public List<SessionListResponse> getSessions(Integer status, Long userId) {
        List<Session> sessions = (status == null)
                ? sessionRepository.findAll()
                : sessionRepository.findByStatus(status);

        return sessions.stream()
            .map(session -> {
                int currentCount = participantRepository.countBySession_SessionId(session.getSessionId());
                
                // 로그인 상태일 경우 내가 참여했는지 확인
                boolean joined = false;
                if (userId != null) {
                    joined = participantRepository.existsBySession_SessionIdAndUser_UserId(
                        session.getSessionId(), userId
                    );
                }
                
                // DTO 인자 3개 전달 (session, currentParticipants, joined)
                return SessionListResponse.from(session, currentCount, joined);
            })
            .collect(Collectors.toList());
    }

    // 3. 내가 참여 중인 세션
    public List<SessionListResponse> getMySessions(Long userId) {
        List<Session> sessions = sessionRepository.findMySessions(userId.intValue());

        return sessions.stream()
            .map(session -> {
                int currentCount = participantRepository.countBySession_SessionId(session.getSessionId());
                // 내 세션 목록이므로 joined는 항상 true
                return SessionListResponse.from(session, currentCount, true);
            })
            .collect(Collectors.toList());
    }

    // 4. 세션 상세 조회
    public SessionDetailResponse getSession(Long sessionId) {
    Session session = sessionRepository.findById(sessionId)
        .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));
    return SessionDetailResponse.from(session);
    }

    // 5. 세션 참여 신청 및 자동 학습 트리거
    @Transactional
    public SessionJoinResponse joinSession(Long sessionId, Long userId) {
        // 1. 세션 존재 검증
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));

        // 2. 사용자 존재 검증
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 3. 중복 참여 검증
        if (participantRepository.existsBySession_SessionIdAndUser_UserId(sessionId, userId)) {
            throw new IllegalStateException("이미 참여 신청한 세션입니다.");
        }

        // 4. 인원 및 정원 체크
        int currentCount = participantRepository.countBySession_SessionId(sessionId);
        if (currentCount >= session.getMaxParticipants()) {
            throw new IllegalStateException("이미 정원이 가득 찼습니다.");
        }

        // 5. 참여 정보 저장
        participantRepository.save(new SessionParticipant(user, session));
        int updatedCount = currentCount + 1;
        session.setParticipantCount(updatedCount);
        sessionRepository.save(session);

        // 6. 상태 업데이트 및 학습 트리거
        String responseStatus = "WAITING"; // 기본 상태
        
        if (updatedCount == session.getMaxParticipants()) {
            session.setStatus(1); // 1: IN_PROGRESS (명세서 switch문 기준)
            responseStatus = "IN_PROGRESS";
            learningService.sendStartRequestToFlower(session);
        } else {
            // 참여자가 생겼지만 아직 정원 미달인 경우
            session.setStatus(0); // 여전히 WAITING
            responseStatus = "WAITING";
        }

        return new SessionJoinResponse(
            sessionId, 
            updatedCount, 
            session.getMaxParticipants(), 
            responseStatus
        );
    }
}