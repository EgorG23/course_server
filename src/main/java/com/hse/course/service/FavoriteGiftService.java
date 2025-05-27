package com.hse.course.service;

import com.google.gson.Gson;
import com.hse.course.model.*;
import com.hse.course.repository.FavoriteRepository;
import com.hse.course.repository.GiftRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteGiftService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final GiftRepository giftRepository;
    private final Gson gson=new Gson();

    @Transactional
    public ApiResponse removeFromFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User is not found"));

        favoriteRepository.deleteByUserAndGiftId(user, productId);
        return new ApiResponse("Removed from favorites", true);
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
        List<FavoriteGift> favorites = favoriteRepository.findByUserId(userId);
        List<Gift> response = new ArrayList<>();
        for(FavoriteGift fg: favorites){
            Gift g = fg.getGift();
            g.setIsFavorite(true);
            response.add(g);
        }
        return new ApiResponse(gson.toJson(response), true);
    }
}