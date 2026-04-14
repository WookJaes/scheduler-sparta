package com.wookjae.scheduler.service;

import com.wookjae.scheduler.dto.CommentResponse;
import com.wookjae.scheduler.dto.CreateScheduleRequest;
import com.wookjae.scheduler.dto.CreateScheduleResponse;
import com.wookjae.scheduler.dto.DeleteScheduleRequest;
import com.wookjae.scheduler.dto.GetScheduleDetailResponse;
import com.wookjae.scheduler.dto.GetScheduleResponse;
import com.wookjae.scheduler.dto.UpdateScheduleRequest;
import com.wookjae.scheduler.dto.UpdateScheduleResponse;
import com.wookjae.scheduler.entity.Comment;
import com.wookjae.scheduler.entity.Schedule;
import com.wookjae.scheduler.repository.CommentRepository;
import com.wookjae.scheduler.repository.ScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * 일정 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 일정 생성, 조회, 수정, 삭제 기능을 처리한다.
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    /**
     * 새로운 일정을 생성한다.
     * 입력값을 검증한 뒤 일정을 저장한다.
     *
     * @param request 일정 생성 요청 데이터
     * @return 생성된 일정 정보
     */
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {
        validateCreateRequest(request);

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

    /**
     * 전체 일정 목록을 조회한다.
     * 작성자(author)가 주어지면 해당 작성자의 일정만 조회한다.
     *
     * @param author 작성자 필터 (선택 값)
     * @return 일정 목록
     */
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

    /**
     * 특정 일정의 상세 정보를 조회한다.
     * 일정 정보와 해당 일정의 댓글 목록을 포함한다.
     *
     * @param scheduleId 조회할 일정의 ID
     * @return 일정 상세 정보
     */
    @Transactional(readOnly = true)
    public GetScheduleDetailResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        List<Comment> comments = commentRepository.findByScheduleId(scheduleId);

        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponses.add(new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreatedAt()
            ));
        }

        return new GetScheduleDetailResponse(
            schedule.getScheduleId(),
            schedule.getTitle(),
            schedule.getContent(),
            schedule.getAuthor(),
            schedule.getCreatedAt(),
            schedule.getModifiedAt(),
            commentResponses
        );
    }

    /**
     * 특정 일정을 수정한다.
     * 입력값을 검증하고 비밀번호를 확인한 뒤 일정을 수정한다.
     *
     * @param scheduleId 수정할 일정의 ID
     * @param request 일정 수정 요청 데이터
     * @return 수정된 일정 정보
     */
    @Transactional
    public UpdateScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 없습니다.")
        );

        validateUpdateRequest(request);

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        schedule.updateSchedule(request.getTitle(), request.getAuthor());
        return new UpdateScheduleResponse(
            schedule.getScheduleId(),
            schedule.getTitle(),
            schedule.getAuthor(),
            schedule.getCreatedAt()
        );
    }

    /**
     * 특정 일정을 삭제한다.
     * 비밀번호를 확인한 뒤 일정을 삭제한다.
     *
     * @param scheduleId 삭제할 일정의 ID
     * @param request 삭제 요청 데이터 (비밀번호 포함)
     */
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

    /**
     * 일정 생성 데이터 유효성 검증
     *
     * @param request 일정 생성 요청 데이터
     */
    private void validateCreateRequest(CreateScheduleRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다.");
        }
        if (request.getTitle().length() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 30자 이내여야 합니다.");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 내용은 필수입니다.");
        }
        if (request.getContent().length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 내용은 200자 이내여야 합니다.");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자명은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }
    }

    /**
     * 일정 수정 데이터 유효성 검증
     *
     * @param request 일정 수정 요청 데이터
     */
    private void validateUpdateRequest(UpdateScheduleRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다.");
        }
        if (request.getTitle().length() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 제목은 30자 이내여야 합니다.");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자명은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }
    }
}