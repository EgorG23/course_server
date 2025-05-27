package com.hse.course.service;

import com.hse.course.dto.OrderRequest;
import com.hse.course.dto.OrderResponse;
import com.hse.course.model.Order;
import com.hse.course.repository.GiftRepository;
import com.hse.course.repository.OrderRepository;
import com.hse.course.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftRepository giftRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new RuntimeException("User is not found");
        }
        if (!giftRepository.existsById(request.getIdAdvertisement())) {
            throw new RuntimeException("Gift is not found");
        }
        Order order = new Order();
        order.setGlobalId(request.getGlobalId());
        order.setUserId(request.getUserId());
        order.setIdAdvertisement(request.getIdAdvertisement());
        order.setAmount(request.getAmount());
        order.setAddress(request.getAddress());
        order.setByCash(request.getIsByCash());

        orderRepository.save(order);
        return OrderResponse.builder()
                .globalId(order.getGlobalId())
                .userId(order.getUserId())
                .idAdvertisement(order.getIdAdvertisement())
                .amount(order.getAmount())
                .address(order.getAddress())
                .isByCash(order.getByCash())
                .build();
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .globalId(order.getGlobalId())
                        .userId(order.getUserId())
                        .idAdvertisement(order.getIdAdvertisement())
                        .amount(order.getAmount())
                        .address(order.getAddress())
                        .isByCash(order.getByCash())
                        .build())
                .toList();
    }
}