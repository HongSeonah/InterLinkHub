package com.hongseonah.interlinkhub.domain.dashboard.service;

import com.hongseonah.interlinkhub.domain.dashboard.dto.response.DashboardFailureResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.DashboardSummaryResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.LatencyStatsResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.ProtocolStatResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.SystemStatResponse;
import java.util.List;

public interface DashboardService {

    DashboardSummaryResponse getSummary();

    List<DashboardFailureResponse> getFailures(int limit);

    List<ProtocolStatResponse> getProtocolStats();

    List<SystemStatResponse> getSystemStats();

    LatencyStatsResponse getLatencyStats();
}