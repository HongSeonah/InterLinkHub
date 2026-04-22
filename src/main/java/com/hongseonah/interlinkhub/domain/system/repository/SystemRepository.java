package com.hongseonah.interlinkhub.domain.system.repository;

import com.hongseonah.interlinkhub.domain.system.entity.ManagedSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRepository extends JpaRepository<ManagedSystem, Long> {

    boolean existsBySystemCode(String systemCode);

    Page<ManagedSystem> findBySystemNameContainingIgnoreCaseOrSystemCodeContainingIgnoreCase(
            String systemName,
            String systemCode,
            Pageable pageable
    );
}