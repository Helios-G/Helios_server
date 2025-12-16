package com.example.helios.member.service;

import com.example.helios.member.domain.Hospital;
import com.example.helios.member.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    // 전체 병원 조회
    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }

    // ID로 조회
    public Optional<Hospital> findById(Integer id) {
        return hospitalRepository.findById(id);
    }

    // 이메일로 조회 (로그인 테스트용)
    public Optional<Hospital> findByEmail(String email) {
        return hospitalRepository.findByEmail(email);
    }
}
