package com.hse.course.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Data
public class MLRequest {
    private List<String> interests;
    private long timestamp;
}