package com.wookjae.scheduler.service;

import com.wookjae.scheduler.dto.CreateCommentRequest;
import com.wookjae.scheduler.dto.CreateCommentResponse;
import com.wookjae.scheduler.entity.Comment;
import com.wookjae.scheduler.repository.CommentRepository;
import com.wookjae.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * 댓글 생성 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 특정 일정에 댓글을 생성한다.
     * 일정 존재 여부를 확인하고, 댓글 입력값을 검증한 뒤 댓글 개수 제한을 확인하여 댓글을 저장한다.
     *
     * @param scheduleId 댓글이 작성될 일정의 ID
     * @param request    댓글 생성 요청 데이터
     * @return 저장된 댓글 정보
     */
    @Transactional
    public CreateCommentResponse save(Long scheduleId, CreateCommentRequest request) {
        scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다.")
        );

        validateCommentRequest(request);

        long count = commentRepository.countByScheduleId(scheduleId);
        if (count >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글은 최대 10개까지만 작성할 수 있습니다.");
        }

        Comment comment = new Comment(
            scheduleId,
            request.getContent(),
            request.getAuthor(),
            request.getPassword()
        );

        Comment savedComment = commentRepository.save(comment);
        return new CreateCommentResponse(
            savedComment.getCommentId(),
            savedComment.getScheduleId(),
            savedComment.getContent(),
            savedComment.getAuthor(),
            savedComment.getCreatedAt(),
            savedComment.getModifiedAt()
        );
    }

    /**
     * 댓글 생성 데이터 유효성 검증
     *
     * @param request 댓글 생성 요청 데이터
     */
    private void validateCommentRequest(CreateCommentRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 내용은 필수입니다.");
        }
        if (request.getContent().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 내용은 100자 이내여야 합니다.");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자명은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수입니다.");
        }
    }
}