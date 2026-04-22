package com.hongseonah.interlinkhub.domain.log.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.log.dto.response.LogPayloadResponse;
import com.hongseonah.interlinkhub.domain.log.dto.response.LogResponse;
import java.util.List;

public interface LogService {

    PageResponse<LogResponse> findLogs(Long interfaceId, Long executionId, int page, int size);

    List<LogResponse> findExecutionLogs(Long executionId);

    LogPayloadResponse findPayload(Long executionId);
}