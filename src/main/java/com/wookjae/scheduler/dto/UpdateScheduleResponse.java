package com.wookjae.scheduler.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateScheduleResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateScheduleResponse(Long id, String title, String author, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}