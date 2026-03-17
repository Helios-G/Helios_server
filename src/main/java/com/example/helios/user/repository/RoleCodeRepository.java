package com.example.helios.user.repository;

import com.example.helios.user.entity.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleCodeRepository extends JpaRepository<RoleCode, Integer> {
    Optional<RoleCode> findByUserRoleCodeName(String userRoleCodeName);
}