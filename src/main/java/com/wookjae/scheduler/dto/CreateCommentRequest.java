package com.wookjae.scheduler.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequest {

    private String content;
    private String author;
    private String password;
}