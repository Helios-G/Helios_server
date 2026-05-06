package com.helios.session.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.helios.common.response.ApiResponse;
import com.helios.session.dto.SessionCreateRequest;
import com.helios.session.dto.SessionCreateResponse;
import com.helios.session.dto.SessionDetailResponse;
import com.helios.session.dto.SessionJoinResponse;
import com.helios.session.dto.SessionListResponse;
import com.helios.session.service.SessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // 1. 세션 생성
    @PostMapping
    public ResponseEntity<ApiResponse<SessionCreateResponse>> createSession(
            @RequestBody @Valid SessionCreateRequest request) {
        SessionCreateResponse data = sessionService.createSession(request);
        return ResponseEntity.ok(ApiResponse.of("세션이 생성되었습니다.", data));
    }

    // 2. 세션 목록
    @GetMapping
    public ResponseEntity<ApiResponse<List<SessionListResponse>>> getSessions(
            @RequestParam(required = false) Integer status,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = null;
        if (userDetails != null) {
            userId = Long.parseLong(userDetails.getUsername());
        }
        List<SessionListResponse> data = sessionService.getSessions(status, userId);
        return ResponseEntity.ok(ApiResponse.of("세션 목록 조회 완료되었습니다.", data));
    }

    // 3. 내가 참여 중인 세션
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<SessionListResponse>>> getMySessions(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        List<SessionListResponse> data = sessionService.getMySessions(userId);
        return ResponseEntity.ok(ApiResponse.of("참여 중인 세션 목록 조회 완료되었습니다.", data));
    }

    // 4. 세션 상세 조회
    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<SessionDetailResponse>> getSession(
            @PathVariable Long sessionId) {
        SessionDetailResponse data = sessionService.getSession(sessionId);
        return ResponseEntity.ok(ApiResponse.of("세션 상세 조회 완료되었습니다.", data));
    }

    // 5. 세션 참여 신청
    @PostMapping("/{sessionId}/join")
    public ResponseEntity<ApiResponse<SessionJoinResponse>> joinSession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        SessionJoinResponse data = sessionService.joinSession(sessionId, userId);
        return ResponseEntity.ok(ApiResponse.of("세션 참여 신청 완료되었습니다.", data));
    }
}