package com.hse.course.controller;

import com.hse.course.service.ApiResponse;
import com.hse.course.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ApiResponse getRecommendations(@PathVariable Long userId) {
        return recommendationService.getRecommendations(userId);
    }
}