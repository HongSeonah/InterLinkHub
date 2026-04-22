package com.hongseonah.interlinkhub.domain.system.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.system.dto.request.SystemCreateRequest;
import com.hongseonah.interlinkhub.domain.system.dto.request.SystemUpdateRequest;
import com.hongseonah.interlinkhub.domain.system.dto.response.SystemResponse;

public interface SystemService {

    SystemResponse create(SystemCreateRequest request);

    PageResponse<SystemResponse> findAll(String keyword, int page, int size);

    SystemResponse findById(Long id);

    SystemResponse update(Long id, SystemUpdateRequest request);
}