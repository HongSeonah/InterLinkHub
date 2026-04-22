package com.hongseonah.interlinkhub.domain.dashboard.dto.response;

import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionStatus;
import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import java.time.LocalDateTime;

public record DashboardFailureResponse(
        Long executionId,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        ExecutionStatus executionStatus,
        String errorCode,
        String errorMessage,
        LocalDateTime occurredAt
) {
    public static DashboardFailureResponse from(ManagedExecution execution) {
        return new DashboardFailureResponse(
                execution.getId(),
                execution.getManagedInterface().getId(),
                execution.getManagedInterface().getInterfaceCode(),
                execution.getManagedInterface().getInterfaceName(),
                execution.getExecutionStatus(),
                execution.getErrorCode(),
                execution.getErrorMessage(),
                execution.getEndedAt() != null ? execution.getEndedAt() : execution.getStartedAt()
        );
    }
}