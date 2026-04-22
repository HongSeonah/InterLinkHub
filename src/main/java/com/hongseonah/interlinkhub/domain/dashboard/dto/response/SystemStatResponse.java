package com.hongseonah.interlinkhub.domain.dashboard.dto.response;

public record SystemStatResponse(
        Long systemId,
        String systemName,
        long executionCount,
        long successCount,
        long failureCount
) {
}