package com.wookjae.scheduler.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {

    private String title;
    private String content;
    private String author;
    private String password;
}