package com.hongseonah.interlinkhub.domain.interfaceinfo.dto.response;

public record ProtocolConnectionTestResponse(
        boolean success,
        int statusCode,
        String message,
        String responseBody
) {
}
