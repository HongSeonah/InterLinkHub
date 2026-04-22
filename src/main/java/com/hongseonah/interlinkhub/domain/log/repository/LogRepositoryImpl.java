package com.hongseonah.interlinkhub.domain.log.repository;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import com.hongseonah.interlinkhub.domain.execution.repository.ExecutionLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LogRepositoryImpl implements LogRepository {

    private final ExecutionLogRepository executionLogRepository;

    @Override
    public Page<ExecutionLog> findLogs(Long interfaceId, Long executionId, Pageable pageable) {
        if (executionId != null) {
            return executionLogRepository.findByExecutionIdOrderByCreatedAtDesc(executionId, pageable);
        }
        if (interfaceId != null) {
            return executionLogRepository.findByExecutionManagedInterfaceIdOrderByCreatedAtDesc(interfaceId, pageable);
        }
        return executionLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public List<ExecutionLog> findExecutionLogs(Long executionId) {
        return executionLogRepository.findByExecutionIdOrderByCreatedAtAsc(executionId);
    }
}