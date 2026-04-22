package com.hongseonah.interlinkhub.domain.dashboard.dto.response;

public record DashboardSummaryResponse(
        long totalInterfaceCount,
        long activeInterfaceCount,
        long todayExecutionCount,
        long todaySuccessCount,
        long todayFailureCount,
        long todayTimeoutCount,
        long averageLatencyMillis,
        long maxLatencyMillis
) {
}