package com.mysite.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionEntityRepository extends JpaRepository<SessionEntity, Long> {
}