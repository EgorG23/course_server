package com.hse.course.dto;

import com.hse.course.model.Gift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MLResponse {
    private List<Long> recommendations;
}