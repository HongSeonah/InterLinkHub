package com.hongseonah.interlinkhub.domain.log.dto.response;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import java.time.LocalDateTime;

public record LogResponse(
        Long logId,
        Long executionId,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        String stepName,
        String logLevel,
        String message,
        LocalDateTime createdAt
) {
    public static LogResponse from(ExecutionLog log) {
        return new LogResponse(
                log.getId(),
                log.getExecution().getId(),
                log.getExecution().getManagedInterface().getId(),
                log.getExecution().getManagedInterface().getInterfaceCode(),
                log.getExecution().getManagedInterface().getInterfaceName(),
                log.getStepName(),
                log.getLogLevel().name(),
                log.getMessage(),
                log.getCreatedAt()
        );
    }
}