package com.example.helios.session.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.example.helios.session.dto.LabelingStartRequest;
import com.example.helios.session.dto.LabelingStartResponse;

@Service
public class LabelService {

    public LabelingStartResponse startLabeling(
            Integer sessionId,
            LabelingStartRequest request
    ) {
        // TODO: 나중에 sessionId 일치 검증

        // ✅ AI 서버 대신 임시 라벨링 세션 ID 생성
        String labelingSessionId = "ls-" + UUID.randomUUID().toString().substring(0, 6);

        return new LabelingStartResponse(
                labelingSessionId,
                sessionId.longValue()
        );
    }
}
