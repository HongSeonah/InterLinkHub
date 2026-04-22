package com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.AuthType;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceProtocolConfig;
import java.time.LocalDateTime;

public record ProtocolConfigResponse(
        Long id,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        String httpMethod,
        String endpointUrl,
        AuthType authType,
        String authValue,
        Integer connectTimeoutMillis,
        Integer readTimeoutMillis,
        Integer retryCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProtocolConfigResponse from(InterfaceProtocolConfig config) {
        return new ProtocolConfigResponse(
                config.getId(),
                config.getManagedInterface().getId(),
                config.getManagedInterface().getInterfaceCode(),
                config.getManagedInterface().getInterfaceName(),
                config.getHttpMethod(),
                config.getEndpointUrl(),
                config.getAuthType(),
                config.getAuthValue(),
                config.getConnectTimeoutMillis(),
                config.getReadTimeoutMillis(),
                config.getRetryCount(),
                config.getCreatedAt(),
                config.getUpdatedAt()
        );
    }
}