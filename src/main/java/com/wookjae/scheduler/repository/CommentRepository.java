package com.wookjae.scheduler.repository;

import com.wookjae.scheduler.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 일정의 댓글 개수 조회
    long countByScheduleId(Long scheduleId);

    // 특정 일정의 댓글 목록 조회
    List<Comment> findByScheduleId(Long scheduleId);
}