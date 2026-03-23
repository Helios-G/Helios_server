package com.example.helios.session.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.helios.session.entity.Session;
import com.example.helios.session.dto.SessionCreateRequest;
import com.example.helios.session.dto.SessionCreateResponse;
import com.example.helios.session.dto.SessionDetailResponse;
import com.example.helios.session.dto.SessionJoinResponse;
import com.example.helios.session.dto.SessionListResponse;
import com.example.helios.session.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // 1. 세션 생성
    @PostMapping
    public ResponseEntity<SessionCreateResponse> createSession(
            @RequestBody SessionCreateRequest request) {

        Session session = sessionService.createSession(request);
        return ResponseEntity.ok(new SessionCreateResponse(session.getSessionId(), "WAITING"));
    }

    // 2. 세션 목록 (전체 / 상태별)
    @GetMapping
    public ResponseEntity<List<SessionListResponse>> getSessions(
            @RequestParam(required = false) Integer status,
            @AuthenticationPrincipal UserDetails userDetails) { // 1. 인자 추가

        // 2. 로그인 여부에 따른 userId 추출 로직
        Long userId = null;
        if (userDetails != null) {
            userId = Long.parseLong(userDetails.getUsername());
        }

        // 3. 서비스 호출 (status와 userId를 함께 전달)
        return ResponseEntity.ok(sessionService.getSessions(status, userId));
    }

    /*
    // 3. 내가 참여 중인 세션 (토큰 기반)
    @GetMapping("/my")
    public ResponseEntity<List<SessionListResponse>> getMySessions(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // 토큰의 username(PK)을 Long으로 변환하여 전달
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(sessionService.getMySessions(userId));
    }
        */

    //내가 참여중인 세션 조회테스트용
    @GetMapping("/my")
    public ResponseEntity<List<SessionListResponse>> getMySessions(
            @RequestParam Long userId) {
        
        return ResponseEntity.ok(sessionService.getMySessions(userId));
    }

    //4. 세션 상세 조회
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDetailResponse> getSession(
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.getSession(sessionId));
    }

    /*
    // 5. 세션 참여 대기 신청
    @PostMapping("/{sessionId}/join") 
    public ResponseEntity<SessionJoinResponse> joinSession(
        @PathVariable(name = "sessionId") Long sessionId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 인증된 사용자의 ID를 추출하여 전달
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(sessionService.joinSession(sessionId, userId));
    }
        */


    // 임시 테스트용 토큰 제거버전
    @PostMapping("/{sessionId}/join")
    public ResponseEntity<SessionJoinResponse> joinSession(
        @PathVariable(name = "sessionId") Long sessionId,
        @RequestParam Long userId
    ) {
        return ResponseEntity.ok(sessionService.joinSession(sessionId, userId));
    }   
}