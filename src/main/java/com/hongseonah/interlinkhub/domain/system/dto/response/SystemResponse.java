package com.hongseonah.interlinkhub.domain.system.dto.response;

import com.hongseonah.interlinkhub.domain.system.entity.ManagedSystem;
import com.hongseonah.interlinkhub.domain.system.entity.SystemStatus;
import com.hongseonah.interlinkhub.domain.system.entity.SystemType;
import java.time.LocalDateTime;

public record SystemResponse(
        Long id,
        String systemCode,
        String systemName,
        SystemType systemType,
        String description,
        SystemStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SystemResponse from(ManagedSystem system) {
        return new SystemResponse(
                system.getId(),
                system.getSystemCode(),
                system.getSystemName(),
                system.getSystemType(),
                system.getDescription(),
                system.getStatus(),
                system.getCreatedAt(),
                system.getUpdatedAt()
        );
    }
}