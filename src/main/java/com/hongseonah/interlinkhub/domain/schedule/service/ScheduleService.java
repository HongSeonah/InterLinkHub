package com.hongseonah.interlinkhub.domain.schedule.service;

import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleCreateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleStatusUpdateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.response.ScheduleResponse;
import com.hongseonah.interlinkhub.domain.schedule.dto.response.ScheduleRunResponse;

public interface ScheduleService {

    ScheduleResponse create(Long interfaceId, ScheduleCreateRequest request);

    PageResponse<ScheduleResponse> findAll(Long interfaceId, int page, int size);

    ScheduleResponse findById(Long scheduleId);

    ScheduleResponse update(Long scheduleId, ScheduleUpdateRequest request);

    ScheduleResponse updateStatus(Long scheduleId, ScheduleStatusUpdateRequest request);

    ScheduleRunResponse run(Long scheduleId);
}