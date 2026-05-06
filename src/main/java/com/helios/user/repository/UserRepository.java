package com.helios.user.repository;

import com.helios.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//테스트용
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 병원 계정 조회 (로그인 시 사용)
    Optional<User> findByEmail(String email);

    // 상태값으로 병원 목록 조회 (활성/비활성 등)
    // List<User> findByStatus(Integer status);
}
