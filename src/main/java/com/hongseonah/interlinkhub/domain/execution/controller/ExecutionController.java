package com.hongseonah.interlinkhub.domain.execution.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionCreateRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionRetryRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionDetailResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionResponse;
import com.hongseonah.interlinkhub.domain.execution.service.ExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping("/interfaces/{interfaceId}/executions")
    public ApiResponse<ExecutionResponse> execute(
            @PathVariable Long interfaceId,
            @Valid @RequestBody ExecutionCreateRequest request
    ) {
        return ApiResponse.success("실행이 요청되었습니다.", executionService.execute(interfaceId, request));
    }

    @GetMapping("/executions")
    public ApiResponse<PageResponse<ExecutionResponse>> findAll(
            @RequestParam(required = false) Long interfaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(executionService.findAll(interfaceId, page, size));
    }

    @GetMapping("/executions/{executionId}")
    public ApiResponse<ExecutionDetailResponse> findById(@PathVariable Long executionId) {
        return ApiResponse.success(executionService.findById(executionId));
    }

    @PostMapping("/executions/{executionId}/retry")
    public ApiResponse<ExecutionResponse> retry(
            @PathVariable Long executionId,
            @Valid @RequestBody ExecutionRetryRequest request
    ) {
        return ApiResponse.success("재처리가 요청되었습니다.", executionService.retry(executionId, request));
    }
}
