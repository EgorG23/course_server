package com.hse.course.controller;

import com.google.gson.Gson;
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
import java.util.Optional;

@RestController
@RequestMapping("/selection")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final Gson gson = new Gson();

    @PostMapping("/add/")
    public ResponseEntity<ApiResponse> createCollection(
            @RequestBody CollectionRequest request
    ) {

        User user = userRepository.findById(request.getUserGlobalId())
                .orElseThrow(() -> new ResourceNotFoundException("User is not found"));

        return ResponseEntity.ok(collectionService.createCollection(request.getDescription(), request.getName(), user));
    }

    @GetMapping("/get/list/{userGlobalId}")
    public ResponseEntity<ApiResponse> getUserCollections(@PathVariable String userGlobalId) {
        try {
            Long userId = Long.valueOf(userGlobalId);
            List<GiftCollection> responses = collectionService.getUserCollections(userId);
            return ResponseEntity.ok(new ApiResponse(gson.toJson(responses), true));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid user ID format", false));
        }
    }

    @GetMapping("/get/{selectionGlobalId}")
    public ResponseEntity<ApiResponse> getCollection(@PathVariable String selectionGlobalId) {
        try {
            Long selectionId = Long.valueOf(selectionGlobalId);
            Optional<GiftCollection> response = collectionService.getCollection(selectionId);
            return ResponseEntity.ok(new ApiResponse(gson.toJson(response.get()), true));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid selection ID format", false));
        }
    }

    @GetMapping("/get/advertisement/{selectionId}")
    public ResponseEntity<ApiResponse> getAdvertisements(@PathVariable Long selectionId) {
        return ResponseEntity.ok(new ApiResponse(
                gson.toJson(collectionService.getGiftsBySelectionId(selectionId)), true));
    }

//    @PutMapping("/{id}") не будем реализовывать (но работает вне клиента!)
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

    @GetMapping("/recommend/{userId}") //Для проверки получения рекомендаций (пользовался чисто для проверки работы с МЛ)
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
