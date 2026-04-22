package com.hongseonah.interlinkhub.domain.schedule.dto.request;

import com.hongseonah.interlinkhub.domain.schedule.entity.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScheduleUpdateRequest(
        @NotBlank(message = "scheduleName은 필수입니다.")
        String scheduleName,

        @NotBlank(message = "cronExpression은 필수입니다.")
        String cronExpression,

        @NotBlank(message = "timezone은 필수입니다.")
        String timezone,

        @NotNull(message = "status는 필수입니다.")
        ScheduleStatus status
) {
}