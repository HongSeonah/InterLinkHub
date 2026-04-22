package com.hongseonah.interlinkhub.domain.schedule.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.execution.dto.request.ExecutionCreateRequest;
import com.hongseonah.interlinkhub.domain.execution.dto.response.ExecutionResponse;
import com.hongseonah.interlinkhub.domain.execution.entity.TriggerType;
import com.hongseonah.interlinkhub.domain.execution.service.ExecutionService;
import com.hongseonah.interlinkhub.domain.interfaceinfo.entity.ManagedInterface;
import com.hongseonah.interlinkhub.domain.interfaceinfo.repository.InterfaceRepository;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleCreateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleStatusUpdateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.response.ScheduleResponse;
import com.hongseonah.interlinkhub.domain.schedule.dto.response.ScheduleRunResponse;
import com.hongseonah.interlinkhub.domain.schedule.entity.ManagedSchedule;
import com.hongseonah.interlinkhub.domain.schedule.entity.ScheduleStatus;
import com.hongseonah.interlinkhub.domain.schedule.repository.ScheduleRepository;
import com.hongseonah.interlinkhub.global.exception.BusinessException;
import com.hongseonah.interlinkhub.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final InterfaceRepository interfaceRepository;
    private final ExecutionService executionService;

    @Override
    @Transactional
    public ScheduleResponse create(Long interfaceId, ScheduleCreateRequest request) {
        ManagedInterface managedInterface = getInterface(interfaceId);
        ManagedSchedule schedule = new ManagedSchedule();
        schedule.setManagedInterface(managedInterface);
        schedule.setScheduleName(request.scheduleName());
        schedule.setCronExpression(request.cronExpression());
        schedule.setTimezone(request.timezone());
        schedule.setStatus(request.status());
        schedule.setNextRunAt(calculateNextRunAt(request.cronExpression(), request.timezone()));
        return ScheduleResponse.from(scheduleRepository.save(schedule));
    }

    @Override
    public PageResponse<ScheduleResponse> findAll(Long interfaceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ManagedSchedule> result = interfaceId == null
                ? scheduleRepository.findAll(pageable)
                : scheduleRepository.findByManagedInterfaceId(interfaceId, pageable);

        return PageResponse.<ScheduleResponse>builder()
                .content(result.getContent().stream().map(ScheduleResponse::from).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public ScheduleResponse findById(Long scheduleId) {
        return ScheduleResponse.from(getSchedule(scheduleId));
    }

    @Override
    @Transactional
    public ScheduleResponse update(Long scheduleId, ScheduleUpdateRequest request) {
        ManagedSchedule schedule = getSchedule(scheduleId);
        schedule.setScheduleName(request.scheduleName());
        schedule.setCronExpression(request.cronExpression());
        schedule.setTimezone(request.timezone());
        schedule.setStatus(request.status());
        schedule.setNextRunAt(calculateNextRunAt(request.cronExpression(), request.timezone()));
        return ScheduleResponse.from(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public ScheduleResponse updateStatus(Long scheduleId, ScheduleStatusUpdateRequest request) {
        ManagedSchedule schedule = getSchedule(scheduleId);
        schedule.setStatus(request.status());
        return ScheduleResponse.from(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public ScheduleRunResponse run(Long scheduleId) {
        ManagedSchedule schedule = getSchedule(scheduleId);
        ExecutionResponse executionResponse = executionService.execute(
                schedule.getManagedInterface().getId(),
                new ExecutionCreateRequest(
                        "SYSTEM",
                        "SCHEDULE-" + schedule.getId() + "-" + UUID.randomUUID(),
                        java.util.Map.of(
                                "scheduleId", schedule.getId(),
                                "scheduleName", schedule.getScheduleName(),
                                "manualTrigger", true
                        )
                ),
                TriggerType.SCHEDULE
        );

        schedule.setLastRunAt(LocalDateTime.now());
        schedule.setNextRunAt(calculateNextRunAt(schedule.getCronExpression(), schedule.getTimezone()));
        scheduleRepository.save(schedule);

        return ScheduleRunResponse.from(schedule.getId(), schedule.getScheduleName(), executionResponse);
    }

    public List<ManagedSchedule> findDueSchedules() {
        return scheduleRepository.findByStatusAndNextRunAtLessThanEqual(ScheduleStatus.ACTIVE, LocalDateTime.now());
    }

    @Transactional
    public void updateAfterAutoRun(ManagedSchedule schedule) {
        schedule.setLastRunAt(LocalDateTime.now());
        schedule.setNextRunAt(calculateNextRunAt(schedule.getCronExpression(), schedule.getTimezone()));
        scheduleRepository.save(schedule);
    }

    private ManagedSchedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    private ManagedInterface getInterface(Long interfaceId) {
        return interfaceRepository.findById(interfaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERFACE_NOT_FOUND));
    }

    private LocalDateTime calculateNextRunAt(String cronExpression, String timezone) {
        CronExpression cron = CronExpression.parse(cronExpression);
        ZonedDateTime now = ZonedDateTime.now(java.time.ZoneId.of(timezone));
        ZonedDateTime next = cron.next(now);
        return next != null ? next.toLocalDateTime() : null;
    }
}