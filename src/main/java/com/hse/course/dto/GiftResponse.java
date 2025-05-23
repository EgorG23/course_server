package com.hse.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftResponse {
    private Long id;
    private String name;
    private String category;
    private Integer price;
    private Boolean isFavorite;
    private Float sellerDiscount;
    private List<String> url;
    private String brand;
    private String description;
    private Float popularity;
    private Integer quantityReviews;
}
