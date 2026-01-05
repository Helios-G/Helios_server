package com.mysite.sbb;

import com.mysite.sbb.dto.AuthRegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.mysite.sbb.dto.AuthLoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final HospitalRepository hospitalRepository;
    private final AdminRepository adminRepository;
    private final SessionEntityRepository sessionRepository;
    private final HospitalStatusCodeRepository hospitalStatusCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(
            HospitalRepository hospitalRepository,
            AdminRepository adminRepository,
            SessionEntityRepository sessionRepository,
            HospitalStatusCodeRepository hospitalStatusCodeRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.hospitalRepository = hospitalRepository;
        this.adminRepository = adminRepository;
        this.sessionRepository = sessionRepository;
        this.hospitalStatusCodeRepository = hospitalStatusCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Hospital register(@RequestBody AuthRegisterRequest request) {

        if (request.getEmail() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email/password is required");
        }

        // 중복 이메일(있으면 400으로 깔끔하게)
        if (hospitalRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // FK 3개 확인 (없으면 400)
        Admin admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin not found: " + request.getAdminId()));

        SessionEntity session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not found: " + request.getSessionId()));

        HospitalStatusCode statusCode = hospitalStatusCodeRepository.findById(request.getHospitalStatusCodeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "HospitalStatusCode not found: " + request.getHospitalStatusCodeId()));

        Hospital hospital = new Hospital(
                request.getName(),
                request.getBusinessNum(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()), // BCrypt 저장
                "2025-01-01",
                "ACTIVE"
        );

        hospital.setAdmin(admin);
        hospital.setSession(session);
        hospital.setHospitalStatusCode(statusCode);

        try {
            return hospitalRepository.save(hospital);
        } catch (Exception e) {
            // 500으로 뭉개지 말고 이유를 보이게
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Register failed: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public String login(@RequestBody AuthLoginRequest request) {

        if (request.getEmail() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email/password is required");
        }

        Hospital hospital = hospitalRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        // BCrypt 검증
        if (!passwordEncoder.matches(request.getPassword(), hospital.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // (선택) 로그인 시 세션 기록 남기기 - 당신 DB 구조상 session은 "세션/프로젝트" 느낌이라면 이 부분은 빼도 됨
        // SessionEntity loginSession = new SessionEntity();
        // loginSession.setHospital(hospital);
        // sessionRepository.save(loginSession);

        return "LOGIN SUCCESS";
    }
    
    @PostMapping("/logout")
    public String logout() {
        // 토큰 / 세션을 쓰지 않으므로 서버에서 할 일 없음
        return "LOGOUT SUCCESS";
    }
}