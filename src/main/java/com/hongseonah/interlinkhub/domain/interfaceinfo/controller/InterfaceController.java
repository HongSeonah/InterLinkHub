package com.hongseonah.interlinkhub.domain.interfaceinfo.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceStatusUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.InterfaceResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.service.InterfaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interfaces")
@RequiredArgsConstructor
public class InterfaceController {

    private final InterfaceService interfaceService;

    @PostMapping
    public ApiResponse<InterfaceResponse> create(@Valid @RequestBody InterfaceCreateRequest request) {
        return ApiResponse.success("인터페이스가 등록되었습니다.", interfaceService.create(request));
    }

    @GetMapping
    public ApiResponse<PageResponse<InterfaceResponse>> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(interfaceService.findAll(keyword, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<InterfaceResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(interfaceService.findById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<InterfaceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InterfaceUpdateRequest request
    ) {
        return ApiResponse.success("인터페이스가 수정되었습니다.", interfaceService.update(id, request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<InterfaceResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody InterfaceStatusUpdateRequest request
    ) {
        return ApiResponse.success("인터페이스 상태가 수정되었습니다.", interfaceService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        interfaceService.delete(id);
        return ApiResponse.<Void>success("인터페이스가 삭제되었습니다.", null);
    }
}
