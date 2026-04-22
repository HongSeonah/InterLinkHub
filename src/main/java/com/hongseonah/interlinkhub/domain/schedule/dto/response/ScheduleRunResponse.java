package com.hongseonah.interlinkhub.domain.schedule.dto.response;

import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionResponse;

public record ScheduleRunResponse(
        Long scheduleId,
        Long interfaceId,
        Long executionId,
        String scheduleName,
        String executionStatus
) {
    public static ScheduleRunResponse from(Long scheduleId, String scheduleName, ExecutionResponse executionResponse) {
        return new ScheduleRunResponse(
                scheduleId,
                executionResponse.interfaceId(),
                executionResponse.executionId(),
                scheduleName,
                executionResponse.executionStatus().name()
        );
    }
}