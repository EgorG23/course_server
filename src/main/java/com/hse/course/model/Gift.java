package com.hse.course.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@Data
@Table(name = "gifts")
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private Integer price;
    private Float popularity;
    private Boolean isFavorite;
    private Float sellerDiscount;
    private List<String> url;
    private String brand;
    private String description;
    private Integer quantityReviews;
}