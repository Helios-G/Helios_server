package com.example.helios.member.repository;

import com.example.helios.member.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//테스트용
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {

    // 이메일로 병원 계정 조회 (로그인 시 사용)
    Optional<Hospital> findByEmail(String email);

    // 상태값으로 병원 목록 조회 (활성/비활성 등)
    List<Hospital> findByStatus(Integer status);
}
