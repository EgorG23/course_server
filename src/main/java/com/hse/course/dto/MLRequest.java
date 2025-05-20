package com.hse.course.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Data
public class MLRequest {
    private String interests;
    private long timestamp;
}