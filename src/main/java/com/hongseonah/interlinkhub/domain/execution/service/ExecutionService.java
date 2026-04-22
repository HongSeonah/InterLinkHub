package com.hongseonah.interlinkhub.domain.execution.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionCreateRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionRetryRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionDetailResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionResponse;

public interface ExecutionService {

    ExecutionResponse execute(Long interfaceId, ExecutionCreateRequest request);

    PageResponse<ExecutionResponse> findAll(Long interfaceId, int page, int size);

    ExecutionDetailResponse findById(Long executionId);

    ExecutionResponse retry(Long executionId, ExecutionRetryRequest request);
}