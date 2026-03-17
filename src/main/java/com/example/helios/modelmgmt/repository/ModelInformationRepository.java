package com.example.helios.modelmgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.helios.modelmgmt.entity.ModelInformation;
import com.example.helios.session.entity.Session;

public interface ModelInformationRepository 
    extends JpaRepository<ModelInformation, Long> {

    boolean existsBySession_SessionId(Long sessionId);
}
