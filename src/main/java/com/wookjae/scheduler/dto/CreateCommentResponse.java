package com.wookjae.scheduler.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateCommentResponse {

    private final Long id;
    private final Long scheduleId;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CreateCommentResponse(Long id, Long scheduleId, String content, String author,
        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}