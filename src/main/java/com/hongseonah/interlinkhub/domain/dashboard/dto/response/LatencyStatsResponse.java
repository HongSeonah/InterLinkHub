package com.hongseonah.interlinkhub.domain.dashboard.dto.response;

public record LatencyStatsResponse(
        long minLatencyMillis,
        long averageLatencyMillis,
        long maxLatencyMillis,
        long p95LatencyMillis
) {
}