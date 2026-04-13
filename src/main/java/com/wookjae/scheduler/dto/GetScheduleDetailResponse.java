package com.wookjae.scheduler.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class GetScheduleDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<CommentResponse> comments;

    public GetScheduleDetailResponse(Long id, String title, String content, String author,
        LocalDateTime createdAt, LocalDateTime modifiedAt, List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.comments = comments;
    }
}