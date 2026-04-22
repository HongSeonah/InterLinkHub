package com.hongseonah.interlinkhub.domain.interfaceinfo.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.ProtocolConfigResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.service.ProtocolConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interfaces/{interfaceId}/protocol-config")
@RequiredArgsConstructor
public class ProtocolConfigController {

    private final ProtocolConfigService protocolConfigService;

    @PostMapping
    public ApiResponse<ProtocolConfigResponse> create(
            @PathVariable Long interfaceId,
            @Valid @RequestBody ProtocolConfigCreateRequest request
    ) {
        return ApiResponse.success("프로토콜 설정이 등록되었습니다.", protocolConfigService.create(interfaceId, request));
    }

    @GetMapping
    public ApiResponse<ProtocolConfigResponse> findByInterfaceId(@PathVariable Long interfaceId) {
        return ApiResponse.success(protocolConfigService.findByInterfaceId(interfaceId));
    }

    @PutMapping
    public ApiResponse<ProtocolConfigResponse> update(
            @PathVariable Long interfaceId,
            @Valid @RequestBody ProtocolConfigUpdateRequest request
    ) {
        return ApiResponse.success("프로토콜 설정이 수정되었습니다.", protocolConfigService.update(interfaceId, request));
    }
}