package com.hongseonah.interlinkhub.domain.interfaceinfo.service;

import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.ProtocolConfigResponse;

public interface ProtocolConfigService {

    ProtocolConfigResponse create(Long interfaceId, ProtocolConfigCreateRequest request);

    ProtocolConfigResponse findByInterfaceId(Long interfaceId);

    ProtocolConfigResponse update(Long interfaceId, ProtocolConfigUpdateRequest request);
}