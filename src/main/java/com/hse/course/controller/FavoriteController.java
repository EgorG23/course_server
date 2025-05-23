package com.hse.course.controller;

import com.hse.course.dto.FavoriteRequest;
import com.hse.course.model.FavoriteGift;
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

    @GetMapping("get/{userId}")
    public ApiResponse getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavorites(userId);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveFavouriteAdvertisement(
            @RequestBody FavoriteRequest request) {
        try {
            Long userId = Long.valueOf(request.getUserId());
            Long giftId = Long.valueOf(request.getAdvertisementId());

            ApiResponse response = favoriteService.addFavorite(userId, giftId);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid ID format", false));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }
}
