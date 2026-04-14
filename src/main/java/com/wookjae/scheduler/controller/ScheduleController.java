package com.wookjae.scheduler.controller;

import com.wookjae.scheduler.dto.CreateScheduleRequest;
import com.wookjae.scheduler.dto.CreateScheduleResponse;
import com.wookjae.scheduler.dto.DeleteScheduleRequest;
import com.wookjae.scheduler.dto.GetScheduleDetailResponse;
import com.wookjae.scheduler.dto.GetScheduleResponse;
import com.wookjae.scheduler.dto.UpdateScheduleRequest;
import com.wookjae.scheduler.dto.UpdateScheduleResponse;
import com.wookjae.scheduler.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 일정(Schedule) 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 새로운 일정을 생성하는 API
     *
     * @param request 일정 생성 요청 데이터 (작성자, 내용 등)
     * @return 생성된 일정 정보와 HTTP 상태 코드 201(CREATED)
     */
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(
        @RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    /**
     * 전체 일정 목록을 조회하는 API
     *
     * @param author 작성자 기준 필터 (선택 값)
     * @return 일정 목록과 HTTP 상태 코드 200(OK)
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(
        @RequestParam(required = false) String author) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll(author));
    }

    /**
     * 특정 일정의 상세 정보를 조회하는 API
     *
     * @param scheduleId 조회할 일정의 ID
     * @return 일정 상세 정보와 HTTP 상태 코드 200(OK)
     */
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleDetailResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    /**
     * 특정 일정을 수정하는 API
     *
     * @param scheduleId 수정할 일정의 ID
     * @param request 일정 수정 요청 데이터
     * @return 수정된 일정 정보와 HTTP 상태 코드 200(OK)
     */
    @PatchMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
        @PathVariable Long scheduleId,
        @RequestBody UpdateScheduleRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, request));
    }

    /**
     * 특정 일정을 삭제하는 API
     *
     * @param scheduleId 삭제할 일정의 ID
     * @param request 삭제 요청 데이터 (비밀번호 등)
     * @return HTTP 상태 코드 204(NO_CONTENT)
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
        @PathVariable Long scheduleId,
        @RequestBody DeleteScheduleRequest request
    ) {
        scheduleService.delete(scheduleId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}