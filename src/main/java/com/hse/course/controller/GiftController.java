package com.hse.course.controller;

import com.hse.course.model.Gift;
import com.hse.course.repository.GiftRepository;
import com.hse.course.service.GiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
public class GiftController {

    private final GiftRepository giftRepository;
    private final GiftService giftService;


    @GetMapping("/list/get")
    public ResponseEntity<List<Gift>> getAllGifts() {
        List<Gift> gifts = giftRepository.findAll();
        return ResponseEntity.ok(gifts);
    }

    @GetMapping("/list/get/query/{query}")
    public ResponseEntity<List<Gift>> getGiftsByQuery(@PathVariable String query) {
        List<Gift> gifts = giftRepository.findByNameContaining(query);
        return ResponseEntity.ok(gifts);
    }

    @GetMapping("/get/{advertisementGlobalId}")
    public ResponseEntity<Gift> getGiftById(@PathVariable Long advertisementGlobalId) {
        Gift gift = giftRepository.findById(advertisementGlobalId)
                .orElseThrow(() -> new IllegalArgumentException("Gift not found"));
        return ResponseEntity.ok(gift);
    }
}