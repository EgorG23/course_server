package com.hse.course.controller;

import com.hse.course.dto.CreateCollectionRequest;
import com.hse.course.dto.UpdateCollectionRequest;
import com.hse.course.model.GiftCollection;
import com.hse.course.model.User;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
    private CollectionService collectionService;

    @PostMapping
    public ResponseEntity<GiftCollection> createCollection(
            @RequestBody CreateCollectionRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(collectionService.createCollection(request, user));
    }

    @GetMapping
    public List<GiftCollection> getUserCollections(@AuthenticationPrincipal User user) {
        return collectionService.getUserCollections(user.getId());
    }

    @PutMapping("/{id}")
    public GiftCollection updateCollection(
            @PathVariable Long id,
            @RequestBody UpdateCollectionRequest request
    ) {
        return collectionService.updateCollection(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
    }

    @GetMapping("/collections/recommend/{userId}")
    public ApiResponse getRecommendedGifts(
            @PathVariable Long userId,
            @RequestParam(required = false) String interest
    ) {
        return collectionService.getRecommendedGifts(userId, interest);
    }
}
