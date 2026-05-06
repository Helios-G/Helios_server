package com.helios.modelmgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helios.modelmgmt.entity.ModelInformation;

import java.util.Optional;

public interface ModelInformationRepository extends JpaRepository<ModelInformation, Long> {
    boolean existsBySession_SessionId(Long sessionId);
    Optional<ModelInformation> findBySession_SessionId(Long sessionId); // 추가
}