package com.example.helios.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.helios.session.entity.Session;
import com.example.helios.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final SessionRepository sessionRepository;

    @Transactional
    public void deleteSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));

        sessionRepository.delete(session);
    }
}