package com.hongseonah.interlinkhub.domain.execution.repository;

import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<ManagedExecution, Long> {

    Page<ManagedExecution> findByManagedInterfaceId(Long interfaceId, Pageable pageable);

    List<ManagedExecution> findTop20ByOrderByCreatedAtDesc();

    boolean existsByRetryOfExecutionId(Long retryOfExecutionId);
}