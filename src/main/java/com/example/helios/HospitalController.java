package com.mysite.sbb;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;
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

    public HospitalController(
            HospitalRepository hospitalRepository,
            AdminRepository adminRepository,
            SessionEntityRepository sessionRepository,
            HospitalStatusCodeRepository hospitalStatusCodeRepository
    ) {
        this.hospitalRepository = hospitalRepository;
        this.adminRepository = adminRepository;
        this.sessionRepository = sessionRepository;
        this.hospitalStatusCodeRepository = hospitalStatusCodeRepository;
    }

    /** CREATE */
    @PostMapping
    public Hospital create(@RequestBody Map<String, Object> request) {

        Long adminId = toLong(request.get("adminId"));
        Long sessionId = toLong(request.get("sessionId"));
        Long hospitalStatusCodeId = toLong(request.get("hospitalStatusCodeId"));

        String name = (String) request.get("name");
        String businessNum = (String) request.get("businessNum");
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String date = (String) request.get("date");
        String status = (String) request.get("status");

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin not found: " + adminId));

        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not found: " + sessionId));

        HospitalStatusCode statusCode = hospitalStatusCodeRepository.findById(hospitalStatusCodeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "HospitalStatusCode not found: " + hospitalStatusCodeId));

        Hospital hospital = new Hospital(
                name,
                businessNum,
                email,
                password,
                date,
                status
        );

        hospital.setAdmin(admin);
        hospital.setSession(session);
        hospital.setHospitalStatusCode(statusCode);

        return hospitalRepository.save(hospital);
    }

    /** READ - 전체 조회 */
    @GetMapping
    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    /** READ - 단일 조회 */
    @GetMapping("/{id}")
    public Hospital getOne(@PathVariable("id") Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + id));
    }
    
    @PutMapping("/{id}")
    public Hospital update(@PathVariable("id") Long id, @RequestBody Map<String, Object> request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + id));

        if (request.containsKey("name")) hospital.setName((String) request.get("name"));
        if (request.containsKey("businessNum")) hospital.setBusinessNum((String) request.get("businessNum"));
        if (request.containsKey("email")) hospital.setEmail((String) request.get("email"));
        if (request.containsKey("password")) hospital.setPassword((String) request.get("password"));
        if (request.containsKey("date")) hospital.setDate((String) request.get("date"));
        if (request.containsKey("status")) hospital.setStatus((String) request.get("status"));

        return hospitalRepository.save(hospital);
    }

    /** DELETE */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital not found: " + id);
        }
        hospitalRepository.deleteById(id);
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) return Long.valueOf((String) value);
        throw new IllegalArgumentException("Cannot convert value to Long: " + value);
    }
}
