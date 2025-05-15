package com.hse.course.controller;

import com.hse.course.service.ApiResponse;
import com.hse.course.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public ApiResponse searchProducts(@RequestParam String query) {
        return productService.searchProducts(query);
    }

    @GetMapping("/category/{category}")
    public ApiResponse getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }
}
