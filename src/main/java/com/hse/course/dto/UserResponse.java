package com.hse.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long globalId;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private Long dob;
}
