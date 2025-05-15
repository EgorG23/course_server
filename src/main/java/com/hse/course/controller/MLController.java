package com.hse.course.controller;

import com.hse.course.service.MLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/ml")
@RequiredArgsConstructor
public class MLController {
    private MLService mlService;

    @PostMapping("/recommend")
    public List<Long> getRecommendations(@RequestBody Set<Integer> interests) {
        return mlService.getRecommendations(interests);
    }
}