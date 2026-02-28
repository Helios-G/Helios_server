package com.mysite.sbb;

import com.mysite.sbb.dto.AuthRegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.mysite.sbb.dto.AuthLoginRequest;
import jakarta.servlet.http.HttpSession;

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
    public String login(@RequestBody AuthLoginRequest request, jakarta.servlet.http.HttpSession session) {

        if (request.getEmail() == null || request.getPassword() == null) {
            throw new com.mysite.sbb.error.ApiException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "AUTH_400",
                    "email/password is required"
            );
        }

        Hospital hospital = hospitalRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new com.mysite.sbb.error.ApiException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED,
                        "AUTH_401",
                        "Invalid email or password"
                ));

        String rawPw = request.getPassword();
        String storedPw = hospital.getPassword();

        boolean ok;

        // 1) bcrypt로 저장된 경우
        if (storedPw != null && storedPw.startsWith("$2a$") || storedPw.startsWith("$2b$") || storedPw.startsWith("$2y$")) {
            ok = passwordEncoder.matches(rawPw, storedPw);
        } else {
            // 2) 평문으로 저장된 경우(레거시)
            ok = rawPw != null && rawPw.equals(storedPw);

            // 로그인 성공이면 즉시 bcrypt로 마이그레이션
            if (ok) {
                hospital.setPassword(passwordEncoder.encode(rawPw));
                hospitalRepository.save(hospital);
            }
        }

        if (!ok) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }


        if (!passwordEncoder.matches(request.getPassword(), hospital.getPassword())) {
            throw new com.mysite.sbb.error.ApiException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "AUTH_401",
                    "Invalid email or password"
            );
        }

        session.setAttribute("HOSPITAL_ID", hospital.getHospitalId());
        session.setAttribute("HOSPITAL_EMAIL", hospital.getEmail());

        return "LOGIN SUCCESS";
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 삭제 (로그아웃)
        return "LOGOUT SUCCESS";
    }
    
    @GetMapping("/me")
    public Object me(HttpSession session) {
        Object hospitalId = session.getAttribute("HOSPITAL_ID");
        if (hospitalId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return java.util.Map.of(
                "hospitalId", hospitalId,
                "email", session.getAttribute("HOSPITAL_EMAIL")
        );
    }
    
    @PostMapping("/admin/register")
    public Admin adminRegister(@RequestBody Admin request, HttpSession session) {

        // 1) 필수값 체크
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email/password is required");
        }

        // 2) 이메일 중복 체크
        if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // 3) 보안: 최초 1명만 공개 가입 허용, 그 이후는 관리자 로그인 필요
        long adminCount = adminRepository.count();
        if (adminCount > 0) {
            Long adminId = (Long) session.getAttribute("ADMIN_ID");
            if (adminId == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
            }
        }

        // 4) 저장 (BCrypt)
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        return adminRepository.save(admin);
    }
    
    @PostMapping("/admin/login")
    public String adminLogin(@RequestBody AuthLoginRequest request, HttpSession session) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        boolean ok =
                (admin.getPassword() != null && passwordEncoder.matches(request.getPassword(), admin.getPassword()))
;

        if (!ok) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        session.setAttribute("ADMIN_ID", admin.getAdminId());
        session.setAttribute("ADMIN_EMAIL", admin.getEmail());
        return "ADMIN LOGIN SUCCESS";
    }

    @PostMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "ADMIN LOGOUT SUCCESS";
    }
}