package com.hongseonah.interlinkhub.domain.log.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.log.dto.response.LogPayloadResponse;
import com.hongseonah.interlinkhub.domain.log.dto.response.LogResponse;
import com.hongseonah.interlinkhub.domain.log.service.LogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    public ApiResponse<PageResponse<LogResponse>> findLogs(
            @RequestParam(required = false) Long interfaceId,
            @RequestParam(required = false) Long executionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(logService.findLogs(interfaceId, executionId, page, size));
    }

    @GetMapping("/{executionId}")
    public ApiResponse<List<LogResponse>> findExecutionLogs(@PathVariable Long executionId) {
        return ApiResponse.success(logService.findExecutionLogs(executionId));
    }

    @GetMapping("/{executionId}/payload")
    public ApiResponse<LogPayloadResponse> findPayload(@PathVariable Long executionId) {
        return ApiResponse.success(logService.findPayload(executionId));
    }
}