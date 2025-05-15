package com.hse.course.service;

public class ApiResponse {
    private Object data;
    private Boolean success;
    private String message;

    public ApiResponse(Object data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    public ApiResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public Object getData() { return data; }
    public Boolean getSuccess() { return success; }
    public String getMessage() { return message; }
}