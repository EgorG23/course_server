package com.hse.course.repository;

import com.hse.course.model.GiftCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CollectionRepository extends JpaRepository<GiftCollection, Long> {
    List<GiftCollection> findByOwnerId(Long owner);
}