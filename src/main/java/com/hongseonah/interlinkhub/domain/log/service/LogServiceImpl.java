package com.hongseonah.interlinkhub.domain.log.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.execution.entity.ExecutionLog;
import com.hongseonah.interlinkhub.domain.execution.entity.ManagedExecution;
import com.hongseonah.interlinkhub.domain.execution.repository.ExecutionRepository;
import com.hongseonah.interlinkhub.domain.log.dto.response.LogPayloadResponse;
import com.hongseonah.interlinkhub.domain.log.dto.response.LogResponse;
import com.hongseonah.interlinkhub.domain.log.repository.LogRepository;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
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
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ExecutionRepository executionRepository;

    @Override
    public PageResponse<LogResponse> findLogs(Long interfaceId, Long executionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExecutionLog> result = logRepository.findLogs(interfaceId, executionId, pageable);

        return PageResponse.<LogResponse>builder()
                .content(result.getContent().stream().map(LogResponse::from).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public List<LogResponse> findExecutionLogs(Long executionId) {
        return logRepository.findExecutionLogs(executionId).stream()
                .map(LogResponse::from)
                .toList();
    }

    @Override
    public LogPayloadResponse findPayload(Long executionId) {
        ManagedExecution execution = executionRepository.findById(executionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXECUTION_NOT_FOUND));
        return LogPayloadResponse.from(execution);
    }
}