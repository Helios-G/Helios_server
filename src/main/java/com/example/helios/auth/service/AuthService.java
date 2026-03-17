package com.example.helios.auth.service;

import com.example.helios.auth.dto.LoginRequest;
import com.example.helios.auth.dto.SignupRequest;
import com.example.helios.auth.dto.TokenResponse;
import com.example.helios.auth.jwt.JwtTokenProvider;
import com.example.helios.user.entity.RoleCode;
import com.example.helios.user.entity.User;
import com.example.helios.user.repository.RoleCodeRepository;
import com.example.helios.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder; // 중복 제거됨
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        RoleCode userRole = roleCodeRepository.findByUserRoleCodeName("ROLE_USER")
        .orElseGet(() -> {
            RoleCode newRole = new RoleCode("ROLE_USER");
            return roleCodeRepository.save(newRole);
        });
        
        // 비즈니스 번호가 없는 일반 유저를 고려한 생성자 호출
        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        
        user.setRoleCode(userRole);
        userRepository.save(user);
    }

    /**
     * 로그인
     */
    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 권한 정보 추출 (User 엔티티에 RoleCode가 연관되어 있어야 함)
        String role = user.getRoleCode() != null ? user.getRoleCode().getUserRoleCodeName() : "ROLE_USER";

        // 4. JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getEmail(), role);

        return TokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }
}