package com.example.helios.learning.service;

import org.springframework.beans.factory.annotation.Value;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.helios.learning.dto.LearningStartRequest;
import com.example.helios.learning.dto.LearningStartResponse;
import com.example.helios.session.entity.Session;
import com.example.helios.session.repository.SessionParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningService {

    private final SessionParticipantRepository participantRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${ai.server.url}")  // ← 여기! 클래스 필드로
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
            System.out.println("Flower 서버로 학습 시작 요청 성공: Session ID " + session.getSessionId());
        } catch (Exception e) {
            System.err.println("Flower 서버 통신 실패: " + e.getMessage());
        }
    }
}