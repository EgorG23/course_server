package com.hse.course.service;

import com.hse.course.model.FavoriteGift;
import com.hse.course.model.Gift;
import com.hse.course.repository.FavoriteRepository;
import com.hse.course.repository.GiftRepository;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftService {
    private final GiftRepository giftRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public List<Gift> getAllGifts(Long userId) {
        List<Gift> allGifts = giftRepository.findAll();
        List<FavoriteGift> favorites = favoriteRepository.findByUserId(userId);
        Set<Long> favoriteIds = favorites.stream()
                .map(f -> f.getGift().getId())
                .collect(Collectors.toSet());
        allGifts.forEach(gift ->
                gift.setIsFavorite(favoriteIds.contains(gift.getId()))
        );
        return allGifts;
    }

    @Transactional
    public List<Gift> getGiftsByName(String name) {
        return giftRepository.findByNameContaining(name);
    }

    @Transactional
    public Gift createGift(Gift gift) {
        return giftRepository.save(gift);
    }

    @Transactional
    public void deleteGiftById(Long id) {
        if (!giftRepository.existsById(id)) {
            throw new IllegalArgumentException("Gift is not found");
        }
        giftRepository.deleteById(id);
    }

    @Transactional
    public List<Gift> getGiftsByQuery(String query, Long userId){
        List<Gift> gifts = giftRepository.findByNameContaining(query);
        List<FavoriteGift> favorites = favoriteRepository.findByUserId(userId);
        Set<Long> favoriteIds = favorites.stream()
                .map(f -> f.getGift().getId())
                .collect(Collectors.toSet());
        gifts.forEach(gift ->
                gift.setIsFavorite(favoriteIds.contains(gift.getId()))
        );
        return gifts;
    }

}