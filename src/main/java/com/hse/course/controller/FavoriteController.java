package com.hse.course.controller;

import com.google.gson.Gson;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.FavoriteGiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advertisement/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteGiftService favoriteService;
    private final Gson gson = new Gson();

    @PostMapping("/{userId}/{giftId}")
    public ResponseEntity<ApiResponse> addFavorite(
            @PathVariable Long userId,
            @PathVariable Long giftId
    ) {
        return ResponseEntity.ok(new ApiResponse(gson.toJson(favoriteService.addFavorite(userId, giftId)), true));
    }

    @DeleteMapping("/{userId}/{giftId}")
    public ResponseEntity<ApiResponse> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long giftId
    ) {
        return ResponseEntity.ok(new ApiResponse(gson.toJson(favoriteService.removeFromFavorites(userId, giftId)), true));
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<ApiResponse> getFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse(gson.toJson(favoriteService.getFavorites(userId)), true));
    }

}
