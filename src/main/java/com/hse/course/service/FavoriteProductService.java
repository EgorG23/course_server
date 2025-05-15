package com.hse.course.service;

import com.hse.course.model.*;
import com.hse.course.repository.FavoriteRepository;
import com.hse.course.repository.ProductRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteProductService {
    private FavoriteRepository favoriteRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    @Transactional
    public ApiResponse addToFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)  // Добавляем получение пользователя
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (favoriteRepository.existsByUserAndProductId(user, productId)) {
            return new ApiResponse("Product already in favorites", false);  // Правильный синтаксис
        }

        FavoriteProduct favorite = new FavoriteProduct();
        favorite.setUser(user);
        favorite.setProduct(productRepository.findById(productId)  // Получаем продукт
                .orElseThrow(() -> new RuntimeException("Product not found")));

        favoriteRepository.save(favorite);
        return new ApiResponse("Added to favorites", true);
    }

    @Transactional
    public ApiResponse removeFromFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        favoriteRepository.deleteByUserAndProductId(user, productId);
        return new ApiResponse("Removed from favorites", true);
    }

    public ApiResponse getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FavoriteProduct> favorites = favoriteRepository.findByUser(user);
        List<Product> products = favorites.stream()
                .map(FavoriteProduct::getProduct)
                .toList();

        return new ApiResponse(products, true);
    }


    @Transactional
    public ApiResponse addFavorite(Long userId, Long productId) {
        // 1. Проверяем существование пользователя и продукта
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Проверяем, не добавлен ли уже в избранное
        if (favoriteRepository.existsByUserAndProductId(user, productId)) {
            return new ApiResponse("Product already in favorites", false);
        }

        // 3. Создаем и сохраняем запись
        FavoriteProduct favorite = new FavoriteProduct();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);

        return new ApiResponse("Added to favorites", true);
    }

    public ApiResponse getFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> favorites = favoriteRepository.findByUser(user).stream()
                .map(FavoriteProduct::getProduct)
                .collect(Collectors.toList());

        return new ApiResponse(favorites, true);
    }
}