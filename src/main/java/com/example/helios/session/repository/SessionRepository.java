package com.example.helios.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.helios.session.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // 상태별 조회
    List<Session> findByStatus(Integer status);

    // 내가 참여 중인 세션 (병원 기준)
    @Query("""
        select distinct s
        from Session s
        join s.hospitalSessions hs
        where hs.hospital.hospitalId = :hospitalId
    """)
    List<Session> findMySessions(@Param("hospitalId") Integer hospitalId);
}
