package com.hse.course.dto;

import lombok.Data;

@Data
public class FavoriteRequest {
    private String advertisementId;
    private String userId;
}
