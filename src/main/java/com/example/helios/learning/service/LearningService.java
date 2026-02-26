package com.example.helios.learning.service;

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

    // 외부 호출 없이 서비스 내부에서 세션 객체를 받아 바로 시작
    public void sendStartRequestToFlower(Session session) {
        // 1. 참여자 명단 추출
        List<Long> participants = participantRepository.findAllBySession_SessionId(session.getSessionId())
                .stream()
                .map(p -> p.getHospital().getHospitalId())
                .toList();

        // 2. 요청 객체 생성 (라운드 수는 세션 설정값 혹은 기본값 사용)
        int rounds = 5; 
        LearningStartRequest flowerRequest = new LearningStartRequest(participants, rounds);

        // 3. Flower 서버로 전송
        String flowerUrl = "http://localhost:8080/train/start";
        
        try {
            restTemplate.postForEntity(flowerUrl, flowerRequest, LearningStartResponse.class);
            System.out.println("Flower 서버로 학습 시작 요청 성공: Session ID " + session.getSessionId());
        } catch (Exception e) {
            // 자동 시작 실패 시 로그만 남기거나 재시도 로직 필요
            System.err.println("Flower 서버 통신 실패: " + e.getMessage());
        }
    }
}