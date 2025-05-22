package com.hse.course.service;

import com.hse.course.dto.OrderRequest;
import com.hse.course.exceptions.ResourceNotFoundException;
import com.hse.course.model.Advertisement;
import com.hse.course.model.Order;
import com.hse.course.model.User;
import com.hse.course.repository.AdvertisementRepository;
import com.hse.course.repository.OrderRepository;
import com.hse.course.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @Transactional
    public Order placeOrder(OrderRequest request) {
        User user = userRepository.findById(request.getGlobalId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Advertisement advertisement = advertisementRepository.findById(request.getIdAdvertisement())
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));
        Order order = new Order();
        order.setGlobalId(order.getGlobalId());
        order.setUserId(order.getUserId());
        order.setIdAdvertisement(order.getIdAdvertisement());
        order.setAmount(request.getAmount());
        return orderRepository.save(order);
    }

    private Long generateOrderId() {
        return System.currentTimeMillis();
    }
}