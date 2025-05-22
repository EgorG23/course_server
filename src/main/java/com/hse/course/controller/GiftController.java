package com.hse.course.controller;

import com.hse.course.model.Gift;
import com.hse.course.repository.GiftRepository;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/gifts")
@RequiredArgsConstructor
public class GiftController {

    private final GiftRepository giftRepository;

    @GetMapping
    public List<Gift> getAllGifts() {
        return giftRepository.findAll();
    }

    @GetMapping("/{id}")
    public Gift getGiftById(@PathVariable Long id) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gift not found"));
    }

    @GetMapping("/search")
    public List<Gift> searchGifts(@RequestParam String name) {
        return giftRepository.findByNameContaining(name);
    }

    @GetMapping("/category/{category}")
    public List<Gift> getGiftsByCategory(@PathVariable String category) {
        return giftRepository.findByCategory(category);
    }
}