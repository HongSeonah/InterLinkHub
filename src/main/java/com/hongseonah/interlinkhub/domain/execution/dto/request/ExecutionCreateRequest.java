package com.hongseonah.interlinkhub.domain.execution.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record ExecutionCreateRequest(
        @NotBlank(message = "requestedBy는 필수입니다.")
        String requestedBy,

        @NotBlank(message = "traceId는 필수입니다.")
        String traceId,

        Map<String, Object> requestBody
) {
}