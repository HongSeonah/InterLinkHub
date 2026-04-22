package com.hongseonah.interlinkhub.domain.schedule.dto.response;

import com.hongseonah.interlinkhub.domain.schedule.entity.ManagedSchedule;
import com.hongseonah.interlinkhub.domain.schedule.entity.ScheduleStatus;
import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        Long interfaceId,
        String interfaceCode,
        String interfaceName,
        String scheduleName,
        String cronExpression,
        String timezone,
        ScheduleStatus status,
        LocalDateTime lastRunAt,
        LocalDateTime nextRunAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ScheduleResponse from(ManagedSchedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getManagedInterface().getId(),
                schedule.getManagedInterface().getInterfaceCode(),
                schedule.getManagedInterface().getInterfaceName(),
                schedule.getScheduleName(),
                schedule.getCronExpression(),
                schedule.getTimezone(),
                schedule.getStatus(),
                schedule.getLastRunAt(),
                schedule.getNextRunAt(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}