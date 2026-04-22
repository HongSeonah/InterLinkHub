package com.hongseonah.interlinkhub.domain.execution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExecutionRetryRequest(
        @NotBlank(message = "requestedBy는 필수입니다.")
        String requestedBy,

        @NotBlank(message = "reason은 필수입니다.")
        String reason,

        @NotNull(message = "outcome은 필수입니다.")
        RetryOutcome outcome
) {
        public enum RetryOutcome {
                SUCCESS,
                FAILURE
        }
}
