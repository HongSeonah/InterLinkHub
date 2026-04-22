package com.hongseonah.interlinkhub.domain.execution.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ExecutionRetryRequest(
        @NotBlank(message = "requestedBy는 필수입니다.")
        String requestedBy,

        @NotBlank(message = "reason은 필수입니다.")
        String reason
) {
}