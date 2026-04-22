package com.hongseonah.interlinkhub.domain.execution.dto.response;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionStatus;
import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import com.hongseonah.interlinkhub.domain.execution.entity.TriggerType;
import java.time.LocalDateTime;

public record ExecutionResponse(
        Long executionId,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        String traceId,
        TriggerType triggerType,
        ExecutionStatus executionStatus,
        Integer responseStatusCode,
        Long durationMillis,
        String requestedBy,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Long retryOfExecutionId
) {
    public static ExecutionResponse from(ManagedExecution execution) {
        return new ExecutionResponse(
                execution.getId(),
                execution.getManagedInterface().getId(),
                execution.getManagedInterface().getInterfaceCode(),
                execution.getManagedInterface().getInterfaceName(),
                execution.getTraceId(),
                execution.getTriggerType(),
                execution.getExecutionStatus(),
                execution.getResponseStatusCode(),
                execution.getDurationMillis(),
                execution.getRequestedBy(),
                execution.getStartedAt(),
                execution.getEndedAt(),
                execution.getRetryOfExecutionId()
        );
    }
}