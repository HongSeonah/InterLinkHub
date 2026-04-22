package com.hongseonah.interlinkhub.domain.execution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionCreateRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionRetryRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionDetailResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionResponse;
import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLogLevel;
import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionStatus;
import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import com.hongseonah.interlinkhub.domain.execution.entity.TriggerType;
import com.hongseonah.interlinkhub.domain.execution.repository.ExecutionLogRepository;
import com.hongseonah.interlinkhub.domain.execution.repository.ExecutionRepository;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ManagedInterface;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.InterfaceRepository;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExecutionServiceImpl implements ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ExecutionLogRepository executionLogRepository;
    private final InterfaceRepository interfaceRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ExecutionResponse execute(Long interfaceId, ExecutionCreateRequest request) {
        return execute(interfaceId, request, TriggerType.MANUAL);
    }

    @Override
    @Transactional
    public ExecutionResponse execute(Long interfaceId, ExecutionCreateRequest request, TriggerType triggerType) {
        ManagedInterface managedInterface = getInterface(interfaceId);

        ManagedExecution execution = new ManagedExecution();
        execution.setManagedInterface(managedInterface);
        execution.setTraceId(request.traceId());
        execution.setTriggerType(triggerType);
        execution.setExecutionStatus(ExecutionStatus.RUNNING);
        execution.setRequestedBy(request.requestedBy());
        execution.setStartedAt(LocalDateTime.now());
        execution.setRequestBody(toJson(request.requestBody()));

        execution = executionRepository.save(execution);
        addLog(execution, "REQUEST", ExecutionLogLevel.INFO, "실행 요청이 생성되었습니다.");

        boolean success = shouldSucceed(request.requestBody());
        if (success) {
            execution.setExecutionStatus(ExecutionStatus.SUCCESS);
            execution.setResponseStatusCode(200);
            execution.setResponseBody("{\"resultCode\":\"0000\",\"resultMessage\":\"정상 처리\"}");
            execution.setEndedAt(LocalDateTime.now());
            execution.setDurationMillis(calculateDuration(execution));
            executionRepository.save(execution);
            addLog(execution, "RESPONSE", ExecutionLogLevel.INFO, "실행이 정상 완료되었습니다.");
        } else {
            execution.setExecutionStatus(ExecutionStatus.FAILED);
            execution.setResponseStatusCode(500);
            execution.setErrorCode("MOCK_500");
            execution.setErrorMessage("mock execution failed");
            execution.setResponseBody("{\"resultCode\":\"9999\",\"resultMessage\":\"실패\"}");
            execution.setEndedAt(LocalDateTime.now());
            execution.setDurationMillis(calculateDuration(execution));
            executionRepository.save(execution);
            addLog(execution, "ERROR", ExecutionLogLevel.ERROR, "실행이 실패했습니다.");
        }

        return ExecutionResponse.from(execution);
    }

    @Override
    public PageResponse<ExecutionResponse> findAll(Long interfaceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ManagedExecution> result = interfaceId == null
                ? executionRepository.findAll(pageable)
                : executionRepository.findByManagedInterfaceId(interfaceId, pageable);

        return PageResponse.<ExecutionResponse>builder()
                .content(result.getContent().stream().map(ExecutionResponse::from).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public ExecutionDetailResponse findById(Long executionId) {
        ManagedExecution execution = getExecution(executionId);
        List<ExecutionLog> logs = executionLogRepository.findByExecutionIdOrderByCreatedAtAsc(executionId);
        return ExecutionDetailResponse.from(execution, logs);
    }

    @Override
    @Transactional
    public ExecutionResponse retry(Long executionId, ExecutionRetryRequest request) {
        ManagedExecution originalExecution = getExecution(executionId);
        if (originalExecution.getExecutionStatus() != ExecutionStatus.FAILED
                && originalExecution.getExecutionStatus() != ExecutionStatus.TIMEOUT) {
            throw new BusinessException(ErrorCode.EXECUTION_NOT_RETRYABLE);
        }

        ManagedExecution retryExecution = new ManagedExecution();
        retryExecution.setManagedInterface(originalExecution.getManagedInterface());
        retryExecution.setTraceId(originalExecution.getTraceId() + "-RETRY");
        retryExecution.setTriggerType(TriggerType.RETRY);
        retryExecution.setExecutionStatus(ExecutionStatus.RUNNING);
        retryExecution.setRequestedBy(request.requestedBy());
        retryExecution.setStartedAt(LocalDateTime.now());
        retryExecution.setRetryOfExecutionId(originalExecution.getId());
        retryExecution.setRequestBody(originalExecution.getRequestBody());

        retryExecution = executionRepository.save(retryExecution);
        addLog(retryExecution, "RETRY", ExecutionLogLevel.INFO, "재처리가 시작되었습니다. reason=" + request.reason());

        retryExecution.setExecutionStatus(ExecutionStatus.SUCCESS);
        retryExecution.setResponseStatusCode(200);
        retryExecution.setResponseBody("{\"resultCode\":\"0000\",\"resultMessage\":\"재처리 성공\"}");
        retryExecution.setEndedAt(LocalDateTime.now());
        retryExecution.setDurationMillis(calculateDuration(retryExecution));
        executionRepository.save(retryExecution);
        addLog(retryExecution, "RESPONSE", ExecutionLogLevel.INFO, "재처리가 정상 완료되었습니다.");

        return ExecutionResponse.from(retryExecution);
    }

    private ManagedInterface getInterface(Long interfaceId) {
        return interfaceRepository.findById(interfaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERFACE_NOT_FOUND));
    }

    private ManagedExecution getExecution(Long executionId) {
        return executionRepository.findById(executionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXECUTION_NOT_FOUND));
    }

    private void addLog(ManagedExecution execution, String stepName, ExecutionLogLevel level, String message) {
        ExecutionLog log = new ExecutionLog();
        log.setExecution(execution);
        log.setStepName(stepName);
        log.setLogLevel(level);
        log.setMessage(message);
        executionLogRepository.save(log);
    }

    private String toJson(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private long calculateDuration(ManagedExecution execution) {
        if (execution.getStartedAt() == null || execution.getEndedAt() == null) {
            return 0L;
        }
        return java.time.Duration.between(execution.getStartedAt(), execution.getEndedAt()).toMillis();
    }

    private boolean shouldSucceed(Object requestBody) {
        if (requestBody instanceof java.util.Map<?, ?> map) {
            Object forceFail = map.get("forceFail");
            if (forceFail instanceof Boolean bool) {
                return !bool;
            }
            if (forceFail instanceof String text) {
                return !"true".equalsIgnoreCase(text);
            }
        }
        return true;
    }
}