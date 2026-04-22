package com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.InterfaceStatus;
import jakarta.validation.constraints.NotNull;

public record InterfaceStatusUpdateRequest(
        @NotNull(message = "status는 필수입니다.")
        InterfaceStatus status
) {
}