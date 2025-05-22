package com.hse.course.controller;

import com.hse.course.dto.AdvertisementResponse;
import com.hse.course.dto.FavoriteRequest;
import com.hse.course.exceptions.ResourceNotFoundException;
import com.hse.course.model.Advertisement;
import com.hse.course.service.AdvertisementService;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.FavoriteGiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final FavoriteGiftService favoriteGiftService;

    @GetMapping("/get/{advertisementGlobalId}")
    public ResponseEntity<ApiResponse> getAdvertisementById(@PathVariable String advertisementGlobalId) {
        Long id;
        try {
            id = Long.parseLong(advertisementGlobalId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid advertisement ID", false));
        }

        Advertisement ad = advertisementService.getAdvertisementById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));

        return ResponseEntity.ok(new ApiResponse(mapToResponse(ad), true));
    }

    @GetMapping("/short/get/{advertisementGlobalId}")
    public ResponseEntity<ApiResponse> getAdvertisementShortById(@PathVariable String advertisementGlobalId) {
        return getAdvertisementById(advertisementGlobalId);
    }

    @GetMapping("/list/get/")
    public ResponseEntity<ApiResponse> getAllAdvertisements() {
        List<Advertisement> ads = advertisementService.getAllAdvertisements();
        List<AdvertisementResponse> responses = ads.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(responses, true));
    }

    @GetMapping("/list/get/query/{query}")
    public ResponseEntity<ApiResponse> getAdvertisementListByQuery(@PathVariable String query) {
        List<Advertisement> ads = advertisementService.searchAdvertisements(query);
        List<AdvertisementResponse> responses = ads.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(responses, true));
    }

    @GetMapping("/favorite/get/{userGlobalId}")
    public ResponseEntity<ApiResponse> getFavoriteAdvertisements(@PathVariable String userGlobalId) {
        Long userId;
        try {
            userId = Long.parseLong(userGlobalId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid user ID", false));
        }

        ApiResponse response = favoriteGiftService.getFavorites(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favorite/save")
    public ResponseEntity<ApiResponse> saveFavoriteAdvertisement(@RequestBody FavoriteRequest request) {
        Long userId;
        Long advertisementId;

        try {
            userId = Long.parseLong(request.getUserId());
            advertisementId = Long.parseLong(request.getAdvertisementId());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid user or advertisement ID", false));
        }

        ApiResponse response = favoriteGiftService.addFavorite(userId, advertisementId);
        return ResponseEntity.ok(response);
    }

    private AdvertisementResponse mapToResponse(Advertisement ad) {
        return new AdvertisementResponse(
                ad.getId(),
                ad.getPrice(),
                ad.getIsFavorite(),
                ad.getSellerDiscount(),
                ad.getUrl(),
                ad.getBrand(),
                ad.getName(),
                ad.getDescription(),
                ad.getRate(),
                ad.getQuantityReviews()
        );
    }
}