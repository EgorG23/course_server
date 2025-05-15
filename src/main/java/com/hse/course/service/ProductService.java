package com.hse.course.service;

import com.hse.course.model.Product;
import com.hse.course.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ApiResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ApiResponse(products, true);
    }

    public ApiResponse searchProducts(String query) {
        List<Product> products = productRepository.findByNameContaining(query);
        return new ApiResponse(products, true);
    }

    public ApiResponse getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return new ApiResponse(products, true);
    }
}