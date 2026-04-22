package com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceStatus;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ProtocolType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InterfaceUpdateRequest(
        @NotBlank(message = "interfaceName은 필수입니다.")
        @Size(max = 100, message = "interfaceName은 100자 이하여야 합니다.")
        String interfaceName,

        @Size(max = 500, message = "description은 500자 이하여야 합니다.")
        String description,

        @NotNull(message = "protocolType은 필수입니다.")
        ProtocolType protocolType,

        @NotNull(message = "sourceSystemId는 필수입니다.")
        Long sourceSystemId,

        @NotNull(message = "targetSystemId는 필수입니다.")
        Long targetSystemId,

        @NotBlank(message = "ownerName은 필수입니다.")
        @Size(max = 100, message = "ownerName은 100자 이하여야 합니다.")
        String ownerName,

        @NotNull(message = "status는 필수입니다.")
        InterfaceStatus status
) {
}