package com.hse.course.controller;

import com.hse.course.dto.CollectionRequest;
import com.hse.course.exceptions.ResourceNotFoundException;
import com.hse.course.model.GiftCollection;
import com.hse.course.model.User;
import com.hse.course.repository.CollectionRepository;
import com.hse.course.repository.UserRepository;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/selection")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    @PostMapping("/add/")
    public ResponseEntity<GiftCollection> createCollection(
            @RequestBody CollectionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("User is null, authentication was failed");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User is not found"));

        return ResponseEntity.ok(collectionService.createCollection(request.getDescription(), request.getName(), user));
    }

    @GetMapping("/get/{userGlobalId}")
    public ResponseEntity<ApiResponse> getUserCollections(@PathVariable String userGlobalId) {
        try {
            Long userId = Long.valueOf(userGlobalId);
            List<GiftCollection> responses = collectionService.getUserCollections(userId);
            return ResponseEntity.ok(new ApiResponse(responses, true));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid user ID format", false));
        }
    }

//    @PutMapping("/{id}") не будем реализовывать
//    public ResponseEntity<GiftCollection> updateCollection(
//            @PathVariable Long id,
//            @RequestBody UpdateCollectionRequest request,
//            @AuthenticationPrincipal User user
//    ) throws AccessDeniedException {
//        return ResponseEntity.ok(collectionService.updateCollection(id, request, user));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommend/{userId}") //Для проверки получения рекомендаций
    public ResponseEntity<ApiResponse> getRecommendedGifts(
            @PathVariable Long userId,
            @RequestParam(required = false) String interest
    ) {
        return ResponseEntity.ok(collectionService.getRecommendedGifts(userId, interest));
    }

    public ResponseEntity<List<GiftCollection>> getRandomCollections() {
        List<GiftCollection> randomCollections = collectionService.getRandomCollections(5);
        return ResponseEntity.ok(randomCollections);
    }
}