package com.hongseonah.interlinkhub.domain.schedule.job;

import com.hongseonah.interlinkhub.domain.schedule.entity.ManagedSchedule;
import com.hongseonah.interlinkhub.domain.schedule.service.ScheduleServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleJobRunner {

    private final ScheduleServiceImpl scheduleService;

    @Scheduled(fixedDelay = 60000)
    public void runDueSchedules() {
        List<ManagedSchedule> schedules = scheduleService.findDueSchedules();
        for (ManagedSchedule schedule : schedules) {
            scheduleService.run(schedule.getId());
        }
    }
}