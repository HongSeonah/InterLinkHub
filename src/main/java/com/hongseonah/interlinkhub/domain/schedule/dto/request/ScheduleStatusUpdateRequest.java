package com.hongseonah.interlinkhub.domain.schedule.dto.request;

import com.hongseonah.interlinkhub.domain.schedule.entity.ScheduleStatus;
import jakarta.validation.constraints.NotNull;

public record ScheduleStatusUpdateRequest(
        @NotNull(message = "status는 필수입니다.")
        ScheduleStatus status
) {
}