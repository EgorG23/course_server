package com.hse.course.repository;

import com.hse.course.model.FavoriteGift;
import com.hse.course.model.Gift;
import com.hse.course.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteGift, Long> {
    List<FavoriteGift> findByUserId(Long userId);
    boolean existsByUserIdAndGiftId(Long userId, Long giftId);
    void deleteByUserIdAndGiftId(Long userId, Long giftId);
    void deleteByUserAndGiftId(User user, Long giftId);
    List<FavoriteGift> findByUser(User user);
    boolean existsByUserAndGiftId(User user, Long giftId);
}