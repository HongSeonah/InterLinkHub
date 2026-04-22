package com.hongseonah.interlinkhub.domain.log.repository;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogRepository {

    Page<ExecutionLog> findLogs(Long interfaceId, Long executionId, Pageable pageable);

    List<ExecutionLog> findExecutionLogs(Long executionId);
}