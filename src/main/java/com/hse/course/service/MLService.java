package com.hse.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MLService {
    private RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public List<Long> getRecommendations(Set<Integer> interests) {
        try {
            // 1. Формируем запрос
            Map<String, Object> request = Map.of(
                    "interests", interests,
                    "timestamp", System.currentTimeMillis()
            );

            // 2. Отправляем запрос к ML-сервису
            ResponseEntity<List> response = restTemplate.postForEntity(
                    mlServiceUrl + "/recommend",
                    request,
                    List.class
            );

            // 3. Преобразуем ответ
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().stream()
                        .filter(Objects::nonNull)
                        .map(o -> Long.parseLong(o.toString()))
                        .toList();
            }
        } catch (Exception e) {
            throw new RuntimeException("ML service error: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}