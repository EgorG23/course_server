package com.hse.course.dto;

import lombok.Data;
import java.util.List;

@Data
public class AdvertisementResponse {
    private Long id;
    private Integer price;
    private Boolean isFavorite;
    private Float sellerDiscount;
    private List<String> url;
    private String brand;
    private String name;
    private String description;
    private Float rate;
    private Integer quantityReviews;

    public AdvertisementResponse(Long id, Integer price, Boolean isFavorite, Float sellerDiscount, List<String> url, String brand, String name, String description, Float rate, Integer quantityReviews) {
    }
}