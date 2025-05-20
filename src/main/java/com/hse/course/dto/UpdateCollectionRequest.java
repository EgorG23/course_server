package com.hse.course.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class UpdateCollectionRequest {
    private String name;
    private String description;
    private String interests;
}
