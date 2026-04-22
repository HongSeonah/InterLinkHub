package com.hongseonah.interlinkhub.domain.execution.dto.response;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionStatus;
import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import com.hongseonah.interlinkhub.domain.execution.entity.TriggerType;
import java.time.LocalDateTime;
import java.util.List;

public record ExecutionDetailResponse(
        Long executionId,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        String traceId,
        TriggerType triggerType,
        ExecutionStatus executionStatus,
        String requestBody,
        String responseBody,
        Integer responseStatusCode,
        String errorCode,
        String errorMessage,
        Long durationMillis,
        String requestedBy,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Long retryOfExecutionId,
        boolean hasRetryExecution,
        List<ExecutionLogResponse> logs
) {
    public static ExecutionDetailResponse from(ManagedExecution execution, List<ExecutionLog> logs, boolean hasRetryExecution) {
        return new ExecutionDetailResponse(
                execution.getId(),
                execution.getManagedInterface().getId(),
                execution.getManagedInterface().getInterfaceCode(),
                execution.getManagedInterface().getInterfaceName(),
                execution.getTraceId(),
                execution.getTriggerType(),
                execution.getExecutionStatus(),
                execution.getRequestBody(),
                execution.getResponseBody(),
                execution.getResponseStatusCode(),
                execution.getErrorCode(),
                execution.getErrorMessage(),
                execution.getDurationMillis(),
                execution.getRequestedBy(),
                execution.getStartedAt(),
                execution.getEndedAt(),
                execution.getRetryOfExecutionId(),
                hasRetryExecution,
                logs.stream().map(ExecutionLogResponse::from).toList()
        );
    }

    public record ExecutionLogResponse(
            String stepName,
            String logLevel,
            String message,
            LocalDateTime createdAt
    ) {
        public static ExecutionLogResponse from(ExecutionLog log) {
            return new ExecutionLogResponse(
                    log.getStepName(),
                    log.getLogLevel().name(),
                    log.getMessage(),
                    log.getCreatedAt()
            );
        }
    }
}
