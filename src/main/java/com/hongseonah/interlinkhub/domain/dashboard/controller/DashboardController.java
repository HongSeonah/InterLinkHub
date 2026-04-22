package com.hongseonah.interlinkhub.domain.dashboard.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.*;
import com.hongseonah.interlinkhub.domain.dashboard.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        return ApiResponse.success(dashboardService.getSummary());
    }

    @GetMapping("/failures")
    public ApiResponse<List<DashboardFailureResponse>> getFailures(
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ApiResponse.success(dashboardService.getFailures(limit));
    }

    @GetMapping("/protocol-stats")
    public ApiResponse<List<ProtocolStatResponse>> getProtocolStats() {
        return ApiResponse.success(dashboardService.getProtocolStats());
    }

    @GetMapping("/system-stats")
    public ApiResponse<List<SystemStatResponse>> getSystemStats() {
        return ApiResponse.success(dashboardService.getSystemStats());
    }

    @GetMapping("/latency-stats")
    public ApiResponse<LatencyStatsResponse> getLatencyStats() {
        return ApiResponse.success(dashboardService.getLatencyStats());
    }
}