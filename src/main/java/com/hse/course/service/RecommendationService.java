package com.hse.course.service;

import com.hse.course.model.User;
import com.hse.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public RecommendationService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public ApiResponse getRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> request = new HashMap<>();
        request.put("interests", user.getInterest());

        try {
            List<Long> productIds = restTemplate.postForObject(
                    mlServiceUrl,
                    request,
                    List.class
            );

            return new ApiResponse(productIds, true);
        } catch (Exception e) {
            return new ApiResponse("Failed to get recommendations: " + e.getMessage(), false);
        }
    }
}