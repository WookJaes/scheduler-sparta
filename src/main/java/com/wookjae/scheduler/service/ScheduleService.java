package com.wookjae.scheduler.service;

import com.wookjae.scheduler.dto.CreateScheduleRequest;
import com.wookjae.scheduler.dto.CreateScheduleResponse;
import com.wookjae.scheduler.entity.Schedule;
import com.wookjae.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {
        Schedule schedule = new Schedule(
            request.getTitle(),
            request.getContent(),
            request.getAuthor(),
            request.getPassword()
        );

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
            savedSchedule.getId(),
            savedSchedule.getTitle(),
            savedSchedule.getContent(),
            savedSchedule.getAuthor(),
            savedSchedule.getCreatedAt(),
            savedSchedule.getModifiedAt()
        );
    }
}