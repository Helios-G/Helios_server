package com.helios.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helios.user.dto.PasswordUpdateRequest;
import com.helios.user.dto.UserResponse;
import com.helios.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.getMyInfo(userDetails.getUsername());
        return ResponseEntity.ok().body(response);
    }

    // 2. 비밀번호 수정
    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PasswordUpdateRequest request) {
        
        userService.updatePassword(userDetails.getUsername(), request.getPassword());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "비밀번호 변경 완료되었습니다");
        return ResponseEntity.ok(response);
    }

    // 3. 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}