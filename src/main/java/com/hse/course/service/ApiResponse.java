package com.hse.course.service;

import lombok.Getter;

@Getter
public class ApiResponse {
    private Object data;
    private final Boolean success;
    private String message;

    public ApiResponse(Object data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    public ApiResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

}