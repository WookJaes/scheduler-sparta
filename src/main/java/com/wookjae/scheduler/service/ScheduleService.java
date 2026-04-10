package com.wookjae.scheduler.service;

import com.wookjae.scheduler.dto.CreateScheduleRequest;
import com.wookjae.scheduler.dto.CreateScheduleResponse;
import com.wookjae.scheduler.dto.GetScheduleResponse;
import com.wookjae.scheduler.entity.Schedule;
import com.wookjae.scheduler.repository.ScheduleRepository;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> findAll(String author) {
        List<Schedule> schedules;

        if (author == null || author.isBlank()) {
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        } else {
            schedules = scheduleRepository.findByAuthorOrderByModifiedAtDesc(author);
        }

        List<GetScheduleResponse> responses = new ArrayList<>();
        for (Schedule schedule : schedules) {
            responses.add(new GetScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
            ));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public GetScheduleResponse findOne(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다!")
        );

        return new GetScheduleResponse(
            schedule.getId(),
            schedule.getTitle(),
            schedule.getContent(),
            schedule.getAuthor(),
            schedule.getCreatedAt(),
            schedule.getModifiedAt()
        );
    }
}