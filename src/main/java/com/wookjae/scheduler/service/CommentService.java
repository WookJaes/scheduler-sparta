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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

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