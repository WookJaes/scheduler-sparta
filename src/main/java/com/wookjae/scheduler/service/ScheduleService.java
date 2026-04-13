package com.wookjae.scheduler.service;

import com.wookjae.scheduler.dto.CreateScheduleRequest;
import com.wookjae.scheduler.dto.CreateScheduleResponse;
import com.wookjae.scheduler.dto.DeleteScheduleRequest;
import com.wookjae.scheduler.dto.GetScheduleResponse;
import com.wookjae.scheduler.dto.UpdateScheduleRequest;
import com.wookjae.scheduler.dto.UpdateScheduleResponse;
import com.wookjae.scheduler.entity.Schedule;
import com.wookjae.scheduler.repository.ScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다.");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 내용은 필수입니다.");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자명은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        Schedule schedule = new Schedule(
            request.getTitle(),
            request.getContent(),
            request.getAuthor(),
            request.getPassword()
        );

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new CreateScheduleResponse(
            savedSchedule.getScheduleId(),
            savedSchedule.getTitle(),
            savedSchedule.getContent(),
            savedSchedule.getAuthor(),
            savedSchedule.getCreatedAt(),
            savedSchedule.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> findAll(String author) {
        List<Schedule> schedules = (author == null || author.isBlank())
            ? scheduleRepository.findAllByOrderByModifiedAtDesc()
            : scheduleRepository.findByAuthorOrderByModifiedAtDesc(author);

        List<GetScheduleResponse> responses = new ArrayList<>();
        for (Schedule schedule : schedules) {
            responses.add(new GetScheduleResponse(
                schedule.getScheduleId(),
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
    public GetScheduleResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        return new GetScheduleResponse(
            schedule.getScheduleId(),
            schedule.getTitle(),
            schedule.getContent(),
            schedule.getAuthor(),
            schedule.getCreatedAt(),
            schedule.getModifiedAt()
        );
    }

    @Transactional
    public UpdateScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다.");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자명은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        schedule.updateSchedule(request.getTitle(), request.getAuthor());
        return new UpdateScheduleResponse(
            schedule.getScheduleId(),
            schedule.getTitle(),
            schedule.getAuthor(),
            schedule.getCreatedAt(),
            schedule.getModifiedAt()
        );
    }

    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다."));

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.deleteById(scheduleId);
    }
}