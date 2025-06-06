package com.hse.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionResponse {
    private Long globalId;
    private Long userGlobalId;
    private String name;
    private String description;
    private List<Long> advertisementList;
}