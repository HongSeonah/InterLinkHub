package com.hongseonah.interlinkhub.domain.dashboard.service;

import com.hongseonah.interlinkhub.domain.dashboard.dto.response.DashboardFailureResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.DashboardSummaryResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.LatencyStatsResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.ProtocolStatResponse;
import com.hongseonah.interlinkhub.domain.dashboard.dto.response.SystemStatResponse;
import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionStatus;
import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import com.hongseonah.interlinkhub.domain.execution.repository.ExecutionRepository;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceStatus;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ManagedInterface;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ProtocolType;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.InterfaceRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ExecutionRepository executionRepository;
    private final InterfaceRepository interfaceRepository;

    @Override
    public DashboardSummaryResponse getSummary() {
        List<ManagedExecution> executions = executionRepository.findAll();
        LocalDate today = LocalDate.now();

        List<ManagedExecution> todayExecutions = executions.stream()
                .filter(execution -> execution.getStartedAt() != null
                        && execution.getStartedAt().toLocalDate().isEqual(today))
                .toList();

        long totalInterfaceCount = interfaceRepository.count();
        long activeInterfaceCount = interfaceRepository.findAll().stream()
                .filter(managedInterface -> managedInterface.getStatus() == InterfaceStatus.ACTIVE)
                .count();

        long todaySuccessCount = todayExecutions.stream()
                .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.SUCCESS)
                .count();
        long todayFailureCount = todayExecutions.stream()
                .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.FAILED)
                .count();
        long todayTimeoutCount = todayExecutions.stream()
                .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.TIMEOUT)
                .count();

        long averageLatencyMillis = Math.round(todayExecutions.stream()
                .map(ManagedExecution::getDurationMillis)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0));

        long maxLatencyMillis = todayExecutions.stream()
                .map(ManagedExecution::getDurationMillis)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        return new DashboardSummaryResponse(
                totalInterfaceCount,
                activeInterfaceCount,
                todayExecutions.size(),
                todaySuccessCount,
                todayFailureCount,
                todayTimeoutCount,
                averageLatencyMillis,
                maxLatencyMillis
        );
    }

    @Override
    public List<DashboardFailureResponse> getFailures(int limit) {
        return executionRepository.findTop20ByOrderByCreatedAtDesc().stream()
                .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.FAILED
                        || execution.getExecutionStatus() == ExecutionStatus.TIMEOUT)
                .limit(limit)
                .map(DashboardFailureResponse::from)
                .toList();
    }

    @Override
    public List<ProtocolStatResponse> getProtocolStats() {
        List<ManagedExecution> executions = executionRepository.findAll();

        Map<ProtocolType, List<ManagedExecution>> grouped = executions.stream()
                .collect(Collectors.groupingBy(execution -> execution.getManagedInterface().getProtocolType()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    List<ManagedExecution> group = entry.getValue();
                    long executionCount = group.size();
                    long successCount = group.stream()
                            .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.SUCCESS)
                            .count();
                    long failureCount = group.stream()
                            .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.FAILED)
                            .count();
                    long averageLatencyMillis = Math.round(group.stream()
                            .map(ManagedExecution::getDurationMillis)
                            .filter(Objects::nonNull)
                            .mapToLong(Long::longValue)
                            .average()
                            .orElse(0.0));

                    return new ProtocolStatResponse(entry.getKey(), executionCount, successCount, failureCount,
                            averageLatencyMillis);
                })
                .sorted(Comparator.comparing(ProtocolStatResponse::protocolType))
                .toList();
    }

    @Override
    public List<SystemStatResponse> getSystemStats() {
        List<ManagedExecution> executions = executionRepository.findAll();

        Map<Long, List<ManagedExecution>> grouped = executions.stream()
                .collect(Collectors.groupingBy(execution -> execution.getManagedInterface().getSourceSystem().getId()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    List<ManagedExecution> group = entry.getValue();
                    ManagedInterface interfaceSample = group.get(0).getManagedInterface();
                    long executionCount = group.size();
                    long successCount = group.stream()
                            .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.SUCCESS)
                            .count();
                    long failureCount = group.stream()
                            .filter(execution -> execution.getExecutionStatus() == ExecutionStatus.FAILED)
                            .count();
                    return new SystemStatResponse(
                            interfaceSample.getSourceSystem().getId(),
                            interfaceSample.getSourceSystem().getSystemName(),
                            executionCount,
                            successCount,
                            failureCount
                    );
                })
                .sorted(Comparator.comparing(SystemStatResponse::systemId))
                .toList();
    }

    @Override
    public LatencyStatsResponse getLatencyStats() {
        List<Long> latencies = executionRepository.findAll().stream()
                .map(ManagedExecution::getDurationMillis)
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        if (latencies.isEmpty()) {
            return new LatencyStatsResponse(0, 0, 0, 0);
        }

        long min = latencies.get(0);
        long max = latencies.get(latencies.size() - 1);
        long average = Math.round(latencies.stream().mapToLong(Long::longValue).average().orElse(0.0));
        int p95Index = (int) Math.ceil(latencies.size() * 0.95) - 1;
        long p95 = latencies.get(Math.max(p95Index, 0));

        return new LatencyStatsResponse(min, average, max, p95);
    }
}