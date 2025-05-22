package com.hse.course.repository;

import com.hse.course.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByGlobalId(Long globalId);
    List<Order> findByUserId(Long userId);
}