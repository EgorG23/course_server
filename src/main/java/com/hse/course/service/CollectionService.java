package com.hse.course.service;

import com.hse.course.dto.CreateCollectionRequest;
import com.hse.course.dto.MLResponse;
import com.hse.course.dto.UpdateCollectionRequest;
import com.hse.course.exceptions.ResourceNotFoundException;
import com.hse.course.model.Gift;
import com.hse.course.model.GiftCollection;
import com.hse.course.model.User;
import com.hse.course.repository.CollectionRepository;
import com.hse.course.repository.GiftRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final GiftRepository giftRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(CollectionService.class);
    @Value("${ml.service.url}")
    private String mlServiceUrl;

    @Transactional
    public GiftCollection createCollection(String interestText, User owner) {
        log.info("Creating collection for user: {}", owner.getEmail());
        GiftCollection collection = new GiftCollection();
        collection.setName("Подборка для друга");
        collection.setDescription("На основе: " + interestText);
        collection.setOwner(owner);
        log.info("Вызываем getMLRecommendations с интересами: {}", interestText);
        List<Long> recommendedIds = getMLRecommendations(interestText);
        log.info("Recommended IDs: {}", recommendedIds);
        collection.setGifts(giftRepository.findAllById(recommendedIds));
        return collectionRepository.save(collection);
    }

    public List<GiftCollection> getUserCollections(Long userId) {
        return collectionRepository.findByOwnerId(userId);
    }

    @Transactional
    private List<Long> getMLRecommendations(String interests) {
        try {
            List<String> interestList = List.of(interests.split(","));
            Map<String, Object> request = Map.of(
                    "interests", interestList,
                    "timestamp", System.currentTimeMillis()
            );

            ResponseEntity<MLResponse> response = restTemplate.postForEntity(
                    mlServiceUrl + "/recommend",
                    request,
                    MLResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getRecommendations();
            }

            return getDefaultRecommendations();

        } catch (Exception e) {
            log.error("ML service error", e);
            return getDefaultRecommendations();
        }
    }

    private List<Long> getDefaultRecommendations() {
        return giftRepository.findTop3ByOrderByPopularityDesc()
                .stream()
                .map(Gift::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public GiftCollection updateCollection(Long id, UpdateCollectionRequest request, User user)
            throws AccessDeniedException {
        GiftCollection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));

        if (!collection.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not the owner of this collection");
        }

        if (request.getName() != null) {
            collection.setName(request.getName());
        }

        if (request.getDescription() != null) {
            collection.setDescription(request.getDescription());
        }

        if (request.getInterests() != null) {
            collection.setInterests(request.getInterests());
            List<Long> newRecommendations = getMLRecommendations(request.getInterests());
            collection.setGifts(giftRepository.findAllById(newRecommendations));
        }

        return collectionRepository.save(collection);
    }

    public ApiResponse getRecommendedGifts(Long userId, String interest) {
        try {
            List<Long> recommendedIds = getMLRecommendations(interest);
            List<Gift> gifts = giftRepository.findAllById(recommendedIds);
            return new ApiResponse(gifts, true);
        } catch (Exception e) {
            log.error("Failed to get recommendations: ", e);
            return new ApiResponse("Error getting recommendations", false);
        }
    }
    @Transactional
    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }
}