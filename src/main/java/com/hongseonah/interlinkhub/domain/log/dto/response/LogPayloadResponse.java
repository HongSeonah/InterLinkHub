package com.hongseonah.interlinkhub.domain.log.dto.response;

import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import java.time.LocalDateTime;

public record LogPayloadResponse(
        Long executionId,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        String requestBody,
        String responseBody,
        Integer responseStatusCode,
        String errorCode,
        String errorMessage,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Long durationMillis
) {
    public static LogPayloadResponse from(ManagedExecution execution) {
        return new LogPayloadResponse(
                execution.getId(),
                execution.getManagedInterface().getId(),
                execution.getManagedInterface().getInterfaceCode(),
                execution.getManagedInterface().getInterfaceName(),
                execution.getRequestBody(),
                execution.getResponseBody(),
                execution.getResponseStatusCode(),
                execution.getErrorCode(),
                execution.getErrorMessage(),
                execution.getStartedAt(),
                execution.getEndedAt(),
                execution.getDurationMillis()
        );
    }
}