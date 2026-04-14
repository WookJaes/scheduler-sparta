package com.wookjae.scheduler.repository;

import com.wookjae.scheduler.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 전체 일정을 수정일 기준 내림차순으로 조회
    List<Schedule> findAllByOrderByModifiedAtDesc();

    // 특정 작성자의 일정을 수정일 기준 내림차순으로 조회
    List<Schedule> findByAuthorOrderByModifiedAtDesc(String author);
}