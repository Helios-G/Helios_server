package com.helios.learning.service;

import org.springframework.beans.factory.annotation.Value;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.helios.common.exception.BusinessException;
import com.helios.common.exception.ErrorCode;
import com.helios.learning.dto.LearningStartRequest;
import com.helios.learning.dto.LearningStartResponse;
import com.helios.session.entity.Session;
import com.helios.session.repository.SessionParticipantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LearningService {

    private final SessionParticipantRepository participantRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public void sendStartRequestToFlower(Session session) {
        List<Long> participants = participantRepository.findAllBySession_SessionId(session.getSessionId())
                .stream()
                .map(p -> p.getUser().getUserId())
                .toList();

        int rounds = 5;
        LearningStartRequest flowerRequest = new LearningStartRequest(participants, rounds);
        String flowerUrl = aiServerUrl + "/sessions/" + session.getSessionId() + "/start";

        try {
            restTemplate.postForEntity(flowerUrl, flowerRequest, LearningStartResponse.class);
            log.info("Flower 서버 학습 시작 요청 성공: Session ID {}", session.getSessionId());
        } catch (Exception e) {
            log.error("Flower 서버 통신 실패: Session ID {}, 오류: {}", session.getSessionId(), e.getMessage());
            throw new BusinessException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}