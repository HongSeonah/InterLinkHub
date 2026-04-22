package com.hongseonah.interlinkhub.domain.interfaceinfo.dto.request;

import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.AuthType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProtocolConfigCreateRequest(
        @NotBlank(message = "httpMethod는 필수입니다.")
        String httpMethod,

        @NotBlank(message = "endpointUrl은 필수입니다.")
        @Size(max = 500, message = "endpointUrl은 500자 이하여야 합니다.")
        String endpointUrl,

        @NotNull(message = "authType은 필수입니다.")
        AuthType authType,

        @Size(max = 500, message = "authValue는 500자 이하여야 합니다.")
        String authValue,

        Integer connectTimeoutMillis,

        Integer readTimeoutMillis,

        Integer retryCount
) {
}