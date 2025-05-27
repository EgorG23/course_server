package com.hse.course.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long globalId;
    private Long userId;
    private Long idAdvertisement;
    private Integer amount;
    private String address;
    private Boolean isByCash;
}
