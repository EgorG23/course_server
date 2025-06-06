package com.hse.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long globalId;
    private Long userId;
    private Long idAdvertisement;
    private Integer amount;
    private String address;
    private Boolean isByCash;
}
