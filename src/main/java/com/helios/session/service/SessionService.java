package com.helios.session.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;
import com.helios.learning.service.LearningService;
import com.helios.session.dto.SessionCreateRequest;
import com.helios.session.dto.SessionCreateResponse;
import com.helios.session.dto.SessionDetailResponse;
import com.helios.session.dto.SessionJoinResponse;
import com.helios.session.dto.SessionListResponse;
import com.helios.session.entity.Session;
import com.helios.session.entity.SessionParticipant;
import com.helios.session.exception.SessionNotFoundException;
import com.helios.session.repository.SessionParticipantRepository;
import com.helios.session.repository.SessionRepository;
import com.helios.user.entity.User;
import com.helios.user.exception.UserNotFoundException;
import com.helios.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionParticipantRepository participantRepository;
    private final LearningService learningService;

    // 1. 세션 생성
    public SessionCreateResponse createSession(SessionCreateRequest request) {
        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setMaxParticipants(
            request.getMaxParticipants() != null ? request.getMaxParticipants() : 5
        );
        session.setDataFormat(request.getDataFormat());
        session.setLabelClassCount(request.getLabelClassCount());
        session.setStatus(0);
        session.setParticipantCount(0);
        session.setCreatedBy(request.getCreatedBy());

        if (request.getLabelClassList() != null) {
            session.setLabelClassList(
                request.getLabelClassList().stream().collect(Collectors.joining(","))
            );
        }

        Session savedSession = sessionRepository.save(session);

        if (request.getCreatedBy() != null) {
            User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException());
            participantRepository.save(new SessionParticipant(user, savedSession));
            savedSession.setParticipantCount(1);
            sessionRepository.save(savedSession);
        }

        return new SessionCreateResponse(savedSession.getSessionId(), "WAITING");
    }

    // 2. 전체/상태별 세션 목록
    public List<SessionListResponse> getSessions(Integer status, Long userId) {
        List<Session> sessions = (status == null)
                ? sessionRepository.findAll()
                : sessionRepository.findByStatus(status);

        return sessions.stream()
            .map(session -> {
                int currentCount = participantRepository.countBySession_SessionId(session.getSessionId());
                boolean joined = false;
                if (userId != null) {
                    joined = participantRepository.existsBySession_SessionIdAndUser_UserId(
                        session.getSessionId(), userId
                    );
                }
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
                return SessionListResponse.from(session, currentCount, true);
            })
            .collect(Collectors.toList());
    }

    // 4. 세션 상세 조회
    public SessionDetailResponse getSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new SessionNotFoundException(sessionId));
        return SessionDetailResponse.from(session);
    }

    // 5. 세션 참여 신청
    @Transactional
    public SessionJoinResponse joinSession(Long sessionId, Long userId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new SessionNotFoundException(sessionId));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException());

        if (participantRepository.existsBySession_SessionIdAndUser_UserId(sessionId, userId)) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_JOINED);
        }

        int currentCount = participantRepository.countBySession_SessionId(sessionId);
        if (currentCount >= session.getMaxParticipants()) {
            throw new BusinessException(ErrorCode.SESSION_FULL);
        }

        participantRepository.save(new SessionParticipant(user, session));
        int updatedCount = currentCount + 1;
        session.setParticipantCount(updatedCount);
        sessionRepository.save(session);

        String responseStatus = "WAITING";
        if (updatedCount == session.getMaxParticipants()) {
            session.setStatus(1);
            responseStatus = "IN_PROGRESS";
            learningService.sendStartRequestToFlower(session);
        }

        return new SessionJoinResponse(sessionId, updatedCount, session.getMaxParticipants(), responseStatus);
    }
}