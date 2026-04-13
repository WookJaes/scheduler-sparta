package com.wookjae.scheduler.repository;

import com.wookjae.scheduler.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository <Comment, Long> {

    long countByScheduleId(Long scheduleId);

    List<Comment> findByScheduleId(Long scheduleId);
}