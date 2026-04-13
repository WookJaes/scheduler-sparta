package com.wookjae.scheduler.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateScheduleResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;

    public UpdateScheduleResponse(Long id, String title, String author, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
    }
}