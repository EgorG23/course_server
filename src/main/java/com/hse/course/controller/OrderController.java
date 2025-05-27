package com.hse.course.controller;

import com.google.gson.Gson;
import com.hse.course.dto.OrderRequest;
import com.hse.course.dto.OrderResponse;
import com.hse.course.model.Order;
import com.hse.course.repository.OrderRepository;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final Gson gson = new Gson();
    @PostMapping("/placing")
    public ResponseEntity<ApiResponse> placeOrder(@RequestBody OrderRequest request) {
        try {
            OrderResponse response = orderService.placeOrder(request);
            return ResponseEntity.ok(new ApiResponse(gson.toJson(response), true));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Creating order error", false));
        }
    }

    @GetMapping("/list/get/{userGlobalId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable String userGlobalId) {
        try {
            Long userId = Long.valueOf(userGlobalId);
            List<OrderResponse> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse(gson.toJson(orders), true));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid user ID format");
        }
    }

}