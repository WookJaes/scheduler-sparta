package com.wookjae.scheduler.controller;

import com.wookjae.scheduler.dto.CreateCommentRequest;
import com.wookjae.scheduler.dto.CreateCommentResponse;
import com.wookjae.scheduler.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 댓글(Comment) 생성 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 일정에 댓글을 생성하는 API
     *
     * @param scheduleId 댓글이 작성될 일정의 ID
     * @param request    댓글 생성 요청 데이터
     * @return 생성된 댓글 정보 (201 CREATED)
     */
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
        @PathVariable Long scheduleId,
        @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(scheduleId, request));
    }
}