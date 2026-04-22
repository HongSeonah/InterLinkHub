package com.hongseonah.interlinkhub.domain.schedule.controller;

import com.hongseonah.interlinkhub.common.response.ApiResponse;
import com.hongseonah.interlinkhub.common.response.PageResponse;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleCreateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleStatusUpdateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.hongseonah.interlinkhub.domain.schedule.dto.response.ScheduleResponse;
import com.hongseonah.interlinkhub.domain.schedule.dto.response.ScheduleRunResponse;
import com.hongseonah.interlinkhub.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/interfaces/{interfaceId}/schedules")
    public ApiResponse<ScheduleResponse> create(
            @PathVariable Long interfaceId,
            @Valid @RequestBody ScheduleCreateRequest request
    ) {
        return ApiResponse.success("스케줄이 등록되었습니다.", scheduleService.create(interfaceId, request));
    }

    @GetMapping("/schedules")
    public ApiResponse<PageResponse<ScheduleResponse>> findAll(
            @RequestParam(required = false) Long interfaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(scheduleService.findAll(interfaceId, page, size));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ApiResponse<ScheduleResponse> findById(@PathVariable Long scheduleId) {
        return ApiResponse.success(scheduleService.findById(scheduleId));
    }

    @PutMapping("/schedules/{scheduleId}")
    public ApiResponse<ScheduleResponse> update(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequest request
    ) {
        return ApiResponse.success("스케줄이 수정되었습니다.", scheduleService.update(scheduleId, request));
    }

    @PutMapping("/schedules/{scheduleId}/status")
    public ApiResponse<ScheduleResponse> updateStatus(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleStatusUpdateRequest request
    ) {
        return ApiResponse.success("스케줄 상태가 수정되었습니다.", scheduleService.updateStatus(scheduleId, request));
    }

    @PostMapping("/schedules/{scheduleId}/run")
    public ApiResponse<ScheduleRunResponse> run(@PathVariable Long scheduleId) {
        return ApiResponse.success("스케줄 실행이 요청되었습니다.", scheduleService.run(scheduleId));
    }
}
