package com.hse.course.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MLRequest {
    private Set<Integer> interests;
    private long timestamp;
}