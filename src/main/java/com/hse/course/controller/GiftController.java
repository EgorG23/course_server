package com.hse.course.controller;

import com.google.gson.Gson;
import com.hse.course.model.Gift;
import com.hse.course.model.User;
import com.hse.course.repository.FavoriteRepository;
import com.hse.course.repository.GiftRepository;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.GiftService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class GiftController {

    private final GiftRepository giftRepository;
    private final GiftService giftService;
    private final FavoriteRepository favoriteRepository;
    private final Gson gson=new Gson();

    @GetMapping("/list/get/{userId}")
    public ResponseEntity<ApiResponse> getAllGifts(@PathVariable Long userId) {
        List<Gift> gifts = giftService.getAllGifts(userId);
        return ResponseEntity.ok(new ApiResponse(gson.toJson(gifts), true));
    }

    @GetMapping("/list/get/query/{userId}/{query}")
    public ResponseEntity<ApiResponse> getGiftsByQuery(@PathVariable Long userId, @PathVariable String query) {
        List<Gift> gifts = giftService.getGiftsByQuery(query, userId);
        return ResponseEntity.ok(new ApiResponse(gson.toJson(gifts),true));
    }

    @GetMapping("/get/{userId}/{advertisementGlobalId}")
    public ResponseEntity<ApiResponse> getGiftById(@PathVariable Long userId, @PathVariable Long advertisementGlobalId) {
        Gift gift = giftRepository.findById(advertisementGlobalId)
                .orElseThrow(() -> new IllegalArgumentException("Gift is not found"));

        boolean isFav = favoriteRepository.existsByUserIdAndGiftId(userId, advertisementGlobalId);
        gift.setIsFavorite(isFav);
        return ResponseEntity.ok(new ApiResponse(gson.toJson(gift),true));
    }

}