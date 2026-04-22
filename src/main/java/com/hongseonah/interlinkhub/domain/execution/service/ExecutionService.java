package com.hongseonah.interlinkhub.domain.execution.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionCreateRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionRetryRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionDetailResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionResponse;
import com.hongseonah.interlinkhub.domain.execution.entity.TriggerType;

public interface ExecutionService {

    ExecutionResponse execute(Long interfaceId, ExecutionCreateRequest request);

    ExecutionResponse execute(Long interfaceId, ExecutionCreateRequest request, TriggerType triggerType);

    PageResponse<ExecutionResponse> findAll(Long interfaceId, int page, int size);

    ExecutionDetailResponse findById(Long executionId);

    ExecutionResponse retry(Long executionId, ExecutionRetryRequest request);
}