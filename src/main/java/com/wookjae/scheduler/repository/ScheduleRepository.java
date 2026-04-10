package com.wookjae.scheduler.repository;

import com.wookjae.scheduler.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}