package com.mysite.sbb;

import com.mysite.sbb.dto.HospitalMeResponse;
import com.mysite.sbb.dto.PasswordChangeRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    private final HospitalRepository hospitalRepository;
    private final AdminRepository adminRepository;
    private final SessionEntityRepository sessionRepository;
    private final HospitalStatusCodeRepository hospitalStatusCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public HospitalController(
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

    // =========================
    // Auth helpers (중복 제거)
    // =========================
    private Long requireAdmin(HttpSession session) {
        Long adminId = (Long) session.getAttribute("ADMIN_ID");
        if (adminId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
        return adminId;
    }

    private Long requireHospital(HttpSession session) {
        Long hospitalId = (Long) session.getAttribute("HOSPITAL_ID");
        if (hospitalId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return hospitalId;
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) return Long.valueOf((String) value);
        throw new IllegalArgumentException("Cannot convert value to Long: " + value);
    }

    // =========================
    // Admin only
    // =========================

    /** CREATE (추천: 관리자만 생성 가능) */
    @PostMapping
    public Hospital create(@RequestBody Map<String, Object> request, HttpSession session) {
        Long adminId = requireAdmin(session);

        Long sessionId = toLong(request.get("sessionId"));
        Long hospitalStatusCodeId = toLong(request.get("hospitalStatusCodeId"));

        String name = (String) request.get("name");
        String businessNum = (String) request.get("businessNum");
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String encodedPassword = passwordEncoder.encode(password);
        String date = (String) request.get("date");
        String status = (String) request.get("status");

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin not found: " + adminId));

        SessionEntity sessionEntity = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not found: " + sessionId));

        HospitalStatusCode statusCode = hospitalStatusCodeRepository.findById(hospitalStatusCodeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "HospitalStatusCode not found: " + hospitalStatusCodeId));

        Hospital hospital = new Hospital(name, businessNum, email, encodedPassword, date, status);
        hospital.setAdmin(admin);
        hospital.setSession(sessionEntity);
        hospital.setHospitalStatusCode(statusCode);

        return hospitalRepository.save(hospital);
    }

    /** READ - 전체 조회 (관리자만) */
    @GetMapping
    public List<Hospital> getAll(HttpSession session) {
        requireAdmin(session);
        return hospitalRepository.findAll();
    }

    /** UPDATE - 단일 수정 (관리자만) */
    @PutMapping("/{id}")
    public Hospital update(@PathVariable("id") Long id,
                           @RequestBody Map<String, Object> request,
                           HttpSession session) {
        requireAdmin(session);

        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + id));

        // 관리자 수정 가능 필드
        if (request.containsKey("name")) hospital.setName((String) request.get("name"));
        if (request.containsKey("businessNum")) hospital.setBusinessNum((String) request.get("businessNum"));
        if (request.containsKey("email")) hospital.setEmail((String) request.get("email"));
        if (request.containsKey("password")) {
            String raw = (String) request.get("password");
            hospital.setPassword(passwordEncoder.encode(raw));
        }
        if (request.containsKey("date")) hospital.setDate((String) request.get("date"));
        if (request.containsKey("status")) hospital.setStatus((String) request.get("status"));

        return hospitalRepository.save(hospital);
    }

    /** DELETE (관리자만) */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id, HttpSession session) {
        requireAdmin(session);

        if (!hospitalRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + id);
        }
        hospitalRepository.deleteById(id);
    }

    // =========================
    // Hospital only (자기 것만)
    // =========================

    @GetMapping("/me")
    public HospitalMeResponse getMe(HttpSession session) {
        Long hospitalId = requireHospital(session);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + hospitalId));

        return new HospitalMeResponse(
                hospital.getHospitalId(),
                hospital.getName(),
                hospital.getBusinessNum(),
                hospital.getEmail(),
                hospital.getDate(),
                hospital.getStatus()
        );
    }

    /** 병원은 자기 정보만 수정 가능 */
    @PutMapping("/me")
    public Hospital updateMe(@RequestBody Map<String, Object> request, HttpSession session) {
        Long hospitalId = requireHospital(session);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + hospitalId));

        // 병원 수정 가능 필드만 (중요: email/password는 막아둠)
        if (request.containsKey("name")) hospital.setName((String) request.get("name"));
        if (request.containsKey("businessNum")) hospital.setBusinessNum((String) request.get("businessNum"));
        if (request.containsKey("password")) {
            String raw = (String) request.get("password");
            hospital.setPassword(passwordEncoder.encode(raw));
        if (request.containsKey("date")) hospital.setDate((String) request.get("date"));
        if (request.containsKey("status")) hospital.setStatus((String) request.get("status"));
        }

        return hospitalRepository.save(hospital);
    }

    @PostMapping("/me/password")
    public String changePassword(@RequestBody PasswordChangeRequest request, HttpSession session) {
        Long hospitalId = requireHospital(session);

        if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "currentPassword/newPassword is required");
        }
        if (request.getNewPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "newPassword must be at least 8 characters");
        }

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + hospitalId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), hospital.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        hospital.setPassword(passwordEncoder.encode(request.getNewPassword()));
        hospitalRepository.save(hospital);

        return "PASSWORD CHANGED";
    }

    // =========================
    // Public (필요하면 제한 가능)
    // =========================

    /** READ - 단일 조회 (현재는 공개) */
    @GetMapping("/{id}")
    public Hospital getOne(@PathVariable("id") Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + id));
    }

    @GetMapping("/ping")
    public String ping() {
        return "HOSPITAL OK";
    }
}