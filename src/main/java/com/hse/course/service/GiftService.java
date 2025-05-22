package com.hse.course.service;

import com.hse.course.model.Gift;
import com.hse.course.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftService {
    private final GiftRepository giftRepository;

    @Transactional
    public List<Gift> getAllGifts() {
        return giftRepository.findAll();
    }

    @Transactional
    public List<Gift> getGiftsByCategory(String category) {
        return giftRepository.findByCategory(category);
    }

    @Transactional
    public List<Gift> getGiftsByName(String name) {
        return giftRepository.findByNameContaining(name);
    }

    @Transactional
    public Gift getGiftById(Long id) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gift not found"));
    }

    @Transactional
    public Gift createGift(Gift gift) {
        return giftRepository.save(gift);
    }

    @Transactional
    public void deleteGiftById(Long id) {
        if (!giftRepository.existsById(id)) {
            throw new IllegalArgumentException("Gift not found");
        }
        giftRepository.deleteById(id);
    }
}