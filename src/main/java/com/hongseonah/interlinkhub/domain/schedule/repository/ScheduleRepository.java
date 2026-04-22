package com.hongseonah.interlinkhub.domain.schedule.repository;

import com.hongseonah.interlinkhub.domain.schedule.entity.ManagedSchedule;
import com.hongseonah.interlinkhub.domain.schedule.entity.ScheduleStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ManagedSchedule, Long> {

    Page<ManagedSchedule> findByManagedInterfaceId(Long interfaceId, Pageable pageable);

    List<ManagedSchedule> findByStatusAndNextRunAtLessThanEqual(ScheduleStatus status, LocalDateTime nextRunAt);
}