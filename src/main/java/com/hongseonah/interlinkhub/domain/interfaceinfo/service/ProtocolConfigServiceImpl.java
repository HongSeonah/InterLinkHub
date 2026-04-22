package com.hongseonah.interlinkhub.domain.interfaceinfo.service;

import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigCreateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request.ProtocolConfigUpdateRequest;
import com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response.ProtocolConfigResponse;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceProtocolConfig;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.InterfaceRepository;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.ProtocolConfigRepository;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProtocolConfigServiceImpl implements ProtocolConfigService {

    private final ProtocolConfigRepository protocolConfigRepository;
    private final InterfaceRepository interfaceRepository;

    @Override
    @Transactional
    public ProtocolConfigResponse create(Long interfaceId, ProtocolConfigCreateRequest request) {
        if (protocolConfigRepository.existsByManagedInterfaceId(interfaceId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PROTOCOL_CONFIG);
        }

        InterfaceProtocolConfig config = new InterfaceProtocolConfig();
        config.setManagedInterface(interfaceRepository.findById(interfaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERFACE_NOT_FOUND)));
        config.setHttpMethod(request.httpMethod());
        config.setEndpointUrl(request.endpointUrl());
        config.setAuthType(request.authType());
        config.setAuthValue(request.authValue());
        config.setConnectTimeoutMillis(request.connectTimeoutMillis());
        config.setReadTimeoutMillis(request.readTimeoutMillis());
        config.setRetryCount(request.retryCount());

        return ProtocolConfigResponse.from(protocolConfigRepository.save(config));
    }

    @Override
    public ProtocolConfigResponse findByInterfaceId(Long interfaceId) {
        return ProtocolConfigResponse.from(getConfig(interfaceId));
    }

    @Override
    @Transactional
    public ProtocolConfigResponse update(Long interfaceId, ProtocolConfigUpdateRequest request) {
        InterfaceProtocolConfig config = getConfig(interfaceId);
        config.setHttpMethod(request.httpMethod());
        config.setEndpointUrl(request.endpointUrl());
        config.setAuthType(request.authType());
        config.setAuthValue(request.authValue());
        config.setConnectTimeoutMillis(request.connectTimeoutMillis());
        config.setReadTimeoutMillis(request.readTimeoutMillis());
        config.setRetryCount(request.retryCount());
        return ProtocolConfigResponse.from(protocolConfigRepository.save(config));
    }

    private InterfaceProtocolConfig getConfig(Long interfaceId) {
        return protocolConfigRepository.findByManagedInterfaceId(interfaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROTOCOL_CONFIG_NOT_FOUND));
    }
}