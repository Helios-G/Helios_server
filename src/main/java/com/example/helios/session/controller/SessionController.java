package com.example.helios.session.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.helios.session.entity.Session;
import com.example.helios.session.dto.LabelingStartRequest;
import com.example.helios.session.dto.LabelingStartResponse;
import com.example.helios.session.dto.SessionCreateRequest;
import com.example.helios.session.dto.SessionCreateResponse;
import com.example.helios.session.dto.SessionJoinRequest;
import com.example.helios.session.dto.SessionJoinResponse;
import com.example.helios.session.dto.SessionListResponse;
import com.example.helios.session.service.LabelService;
import com.example.helios.session.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final LabelService labelService;

    // 세션 생성
    @PostMapping("/create")
    public ResponseEntity<SessionCreateResponse> createSession(
            @RequestBody SessionCreateRequest request) {

        Session session = sessionService.createSession(request);

        return ResponseEntity.ok(
            new SessionCreateResponse(
                session.getSessionId(),
                "OPEN"
            )
        );
    }

    // 세션 목록 (전체 / 상태별)
    @GetMapping
    public ResponseEntity<List<SessionListResponse>> getSessions(
            @RequestParam(required = false) Integer status) {

        return ResponseEntity.ok(sessionService.getSessions(status));
    }

    // 내가 참여 중인 세션
    @GetMapping("/my")
    public ResponseEntity<List<SessionListResponse>> getMySessions(
            @RequestParam Integer hospitalId) {

        return ResponseEntity.ok(sessionService.getMySessions(hospitalId));
    }

    // 라벨링 시작
    @PostMapping("/{sessionId}/label/start")
    public ResponseEntity<LabelingStartResponse> startLabeling(
            @PathVariable Integer sessionId,
            @RequestBody LabelingStartRequest request
    ) {
        return ResponseEntity.ok(
                labelService.startLabeling(sessionId, request)
        );
    }    

    // 세션 참여 대기 신청
    @PostMapping("/{sessionId}/join") 
    public ResponseEntity<SessionJoinResponse> joinSession(
        @PathVariable(name = "sessionId") Long sessionId,
        @RequestBody SessionJoinRequest request
    ) {
        return ResponseEntity.ok(sessionService.joinSession(sessionId, request));
    }
}
