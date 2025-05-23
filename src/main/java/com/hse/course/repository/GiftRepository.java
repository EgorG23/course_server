package com.hse.course.repository;

import com.hse.course.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    @Query("SELECT g FROM Gift g ORDER BY g.popularity DESC LIMIT 3")
    List<Gift> findTop3ByOrderByPopularityDesc();
    List<Gift> findByNameContaining(String name);
    List<Gift> findByCategory(String category);

    List<Gift> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Gift> findByPopularityGreaterThanEqual(Double minPopularity);
}