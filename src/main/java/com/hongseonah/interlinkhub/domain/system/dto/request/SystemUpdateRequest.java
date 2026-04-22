package com.hongseonah.interlinkhub.domain.system.dto.request;

import com.hongseonah.interlinkhub.domain.system.entity.SystemStatus;
import com.hongseonah.interlinkhub.domain.system.entity.SystemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SystemUpdateRequest(
        @NotBlank(message = "systemName은 필수입니다.")
        @Size(max = 100, message = "systemName은 100자 이하여야 합니다.")
        String systemName,

        @NotNull(message = "systemType은 필수입니다.")
        SystemType systemType,

        @Size(max = 500, message = "description은 500자 이하여야 합니다.")
        String description,

        @NotNull(message = "status는 필수입니다.")
        SystemStatus status
) {
}