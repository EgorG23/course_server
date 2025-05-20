package com.hse.course.service;

import com.hse.course.model.*;
import com.hse.course.repository.FavoriteRepository;
import com.hse.course.repository.GiftRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteGiftService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final GiftRepository giftRepository;

    @Transactional
    public ApiResponse addToFavorites(Long userId, Long giftId) {
        User user = userRepository.findById(userId)  // Добавляем получение пользователя
                .orElseThrow(() -> new RuntimeException("User is not found"));

        if (favoriteRepository.existsByUserAndGiftId(user, giftId)) {
            return new ApiResponse("Gift is already in favorites", false);
        }

        FavoriteGift favorite = new FavoriteGift();
        favorite.setUser(user);
        favorite.setGift(giftRepository.findById(giftId)  // Получаем продукт
                .orElseThrow(() -> new RuntimeException("Gift is not found")));

        favoriteRepository.save(favorite);
        return new ApiResponse("Added to favorites", true);
    }

    @Transactional
    public ApiResponse removeFromFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        favoriteRepository.deleteByUserAndGiftId(user, productId);
        return new ApiResponse("Removed from favorites", true);
    }

    public ApiResponse getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User is not found"));
        List<FavoriteGift> favorites = favoriteRepository.findByUser(user);
        List<Gift> gifts = favorites.stream()
                .map(FavoriteGift::getGift)
                .toList();
        return new ApiResponse(gifts, true);
    }

    @Transactional
    public ApiResponse addFavorite(Long userId, Long giftId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User is not found"));
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new RuntimeException("Gift is not found"));
        if (favoriteRepository.existsByUserAndGiftId(user, giftId)) {
            return new ApiResponse("Gift is already in favorites", false);
        }
        FavoriteGift favorite = new FavoriteGift();
        favorite.setUser(user);
        favorite.setGift(gift);
        favoriteRepository.save(favorite);

        return new ApiResponse("Added to favorites", true);
    }

    public ApiResponse getFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Gift> favorites = favoriteRepository.findByUser(user).stream()
                .map(FavoriteGift::getGift)
                .collect(Collectors.toList());

        return new ApiResponse(favorites, true);
    }
}