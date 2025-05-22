package com.hse.course.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private Long dob;
}