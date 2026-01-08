package com.example.helios.session.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.helios.session.domain.Session;
import com.example.helios.session.dto.SessionCreateRequest;
import com.example.helios.session.dto.SessionCreateResponse;
import com.example.helios.session.dto.SessionJoinRequest;
import com.example.helios.session.dto.SessionJoinResponse;
import com.example.helios.session.dto.SessionListResponse;
import com.example.helios.session.service.SessionService;

import java.util.List; 

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

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

    /*
    // 세션 참여
    @PostMapping("/{sessionId}/join")
    public ResponseEntity<SessionJoinResponse> joinSession(
            @PathVariable Integer sessionId,
            @RequestBody SessionJoinRequest request) {

        return ResponseEntity.ok(
            sessionService.joinSession(sessionId, request)
        );
    }
    */
}
