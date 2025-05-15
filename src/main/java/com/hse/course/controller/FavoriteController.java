package com.hse.course.controller;

import com.hse.course.service.ApiResponse;
import com.hse.course.service.FavoriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// FavoriteController.java - новый контроллер
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private FavoriteProductService favoriteService;

    @PostMapping("/{userId}/{productId}")
    public ApiResponse addFavorite(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        return favoriteService.addFavorite(userId, productId);
    }

    @GetMapping("/{userId}")
    public ApiResponse getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavorites(userId);
    }
}
