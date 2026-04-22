package com.hongseonah.interlinkhub.domain.interfaceinfo.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceStatusUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.InterfaceUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.InterfaceResponse;

public interface InterfaceService {

    InterfaceResponse create(InterfaceCreateRequest request);

    PageResponse<InterfaceResponse> findAll(String keyword, int page, int size);

    InterfaceResponse findById(Long id);

    InterfaceResponse update(Long id, InterfaceUpdateRequest request);

    InterfaceResponse updateStatus(Long id, InterfaceStatusUpdateRequest request);

    void delete(Long id);
}
