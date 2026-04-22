package com.hongseonah.interlinkhub.domain.execution.repository;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {

    List<ExecutionLog> findByExecutionIdOrderByCreatedAtAsc(Long executionId);

    Page<ExecutionLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ExecutionLog> findByExecutionIdOrderByCreatedAtDesc(Long executionId, Pageable pageable);

    Page<ExecutionLog> findByExecutionManagedInterfaceIdOrderByCreatedAtDesc(Long interfaceId, Pageable pageable);
}