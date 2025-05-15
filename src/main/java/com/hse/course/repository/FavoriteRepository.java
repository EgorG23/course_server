package com.hse.course.repository;

import com.hse.course.model.FavoriteProduct;
import com.hse.course.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserAndProductId(User user, Long productId);

    List<FavoriteProduct> findByUser(User user);

    boolean existsByUserAndProductId(User user, Long productId);
}