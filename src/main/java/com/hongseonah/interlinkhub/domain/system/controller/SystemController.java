package com.hongseonah.interlinkhub.domain.system.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.system.dto.request.SystemCreateRequest;
import com.hongseonah.interlinkhub.domain.system.dto.request.SystemUpdateRequest;
import com.hongseonah.interlinkhub.domain.system.dto.response.SystemResponse;
import com.hongseonah.interlinkhub.domain.system.service.SystemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/systems")
@RequiredArgsConstructor
public class SystemController {

    private final SystemService systemService;

    @PostMapping
    public ApiResponse<SystemResponse> create(@Valid @RequestBody SystemCreateRequest request) {
        return ApiResponse.success("시스템이 등록되었습니다.", systemService.create(request));
    }

    @GetMapping
    public ApiResponse<PageResponse<SystemResponse>> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(systemService.findAll(keyword, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<SystemResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(systemService.findById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<SystemResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SystemUpdateRequest request
    ) {
        return ApiResponse.success("시스템이 수정되었습니다.", systemService.update(id, request));
    }
}
