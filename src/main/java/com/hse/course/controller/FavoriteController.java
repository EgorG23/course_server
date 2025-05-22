package com.hse.course.controller;

import com.hse.course.dto.FavoriteRequest;
import com.hse.course.model.FavoriteGift;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.FavoriteGiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advertisement")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteGiftService favoriteService;

    @PostMapping("/{userId}/{giftId}")
    public ApiResponse addFavorite(
            @PathVariable Long userId,
            @PathVariable Long giftId
    ) {
        return favoriteService.addFavorite(userId, giftId);
    }

    @DeleteMapping("/{userId}/{giftId}")
    public ApiResponse removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long giftId
    ) {
        return favoriteService.removeFromFavorites(userId, giftId);
    }

    @GetMapping("/{userId}")
    public ApiResponse getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavorites(userId);
    }

}
