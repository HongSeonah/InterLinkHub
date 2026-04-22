package com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceStatus;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ManagedInterface;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ProtocolType;
import java.time.LocalDateTime;

public record InterfaceResponse(
        Long id,
        String interfaceCode,
        String interfaceName,
        String description,
        ProtocolType protocolType,
        Long sourceSystemId,
        String sourceSystemName,
        Long targetSystemId,
        String targetSystemName,
        InterfaceStatus status,
        String ownerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static InterfaceResponse from(ManagedInterface managedInterface) {
        return new InterfaceResponse(
                managedInterface.getId(),
                managedInterface.getInterfaceCode(),
                managedInterface.getInterfaceName(),
                managedInterface.getDescription(),
                managedInterface.getProtocolType(),
                managedInterface.getSourceSystem().getId(),
                managedInterface.getSourceSystem().getSystemName(),
                managedInterface.getTargetSystem().getId(),
                managedInterface.getTargetSystem().getSystemName(),
                managedInterface.getStatus(),
                managedInterface.getOwnerName(),
                managedInterface.getCreatedAt(),
                managedInterface.getUpdatedAt()
        );
    }
}