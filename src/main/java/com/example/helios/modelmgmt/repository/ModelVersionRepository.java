package com.example.helios.modelmgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.helios.modelmgmt.entity.ModelVersion;

public interface ModelVersionRepository extends JpaRepository<ModelVersion, Long> {
}