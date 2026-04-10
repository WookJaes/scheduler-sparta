package com.wookjae.scheduler.repository;

import com.wookjae.scheduler.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByOrderByModifiedAtDesc();

    List<Schedule> findByAuthorOrderByModifiedAtDesc(String author);
}