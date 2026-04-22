package com.hongseonah.interlinkhub.domain.dashboard.dto.response;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ProtocolType;

public record ProtocolStatResponse(
        ProtocolType protocolType,
        long executionCount,
        long successCount,
        long failureCount,
        long averageLatencyMillis
) {
}