package com.hse.course.model;

import lombok.Data;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "advertisements")
@Data
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}
