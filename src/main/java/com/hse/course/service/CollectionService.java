package com.hse.course.service;

import com.hse.course.dto.CreateCollectionRequest;
import com.hse.course.dto.UpdateCollectionRequest;
import com.hse.course.exceptions.ResourceNotFoundException;
import com.hse.course.model.Gift;
import com.hse.course.model.GiftCollection;
import com.hse.course.model.User;
import com.hse.course.repository.CollectionRepository;
import com.hse.course.repository.GiftRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private CollectionRepository collectionRepository;
    private GiftRepository giftRepository;
    private RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(CollectionService.class);
    @Value("${ml.service.url}")
    private String mlServiceUrl;

    @Transactional
    public GiftCollection createCollection(CreateCollectionRequest request, User owner) {
        GiftCollection collection = new GiftCollection();
        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        collection.setInterests(request.getInterests());
        collection.setOwner(owner);

        List<Long> recommendedGiftIds = getMLRecommendations(request.getInterests());
        List<Gift> recommendedGifts = giftRepository.findAllById(recommendedGiftIds);
        collection.setGifts(recommendedGifts);

        return collectionRepository.save(collection);
    }

    public List<GiftCollection> getUserCollections(Long userId) {
        return collectionRepository.findByOwnerId(userId);
    }

    @Transactional
    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }

    private List<Long> getMLRecommendations(List<String> interests) {
        try {
            Map<String, Object> request = Map.of(
                    "interests", interests,
                    "timestamp", System.currentTimeMillis()
            );

            ResponseEntity<List> response = restTemplate.postForEntity(
                    mlServiceUrl + "/recommend",
                    request,
                    List.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return ((List<?>) response.getBody()).stream()
                        .filter(Objects::nonNull)
                        .map(id -> Long.parseLong(id.toString()))
                        .collect(Collectors.toList());
            }
            return getDefaultRecommendations();
        } catch (Exception e) {
            log.error("ML service error", e);
            return getDefaultRecommendations();
        }
    }

    // Реализация метода для GiftRepository
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
            List<String> interests = interest != null ?
                    Collections.singletonList(interest) :
                    getUserInterests(userId);

            List<Long> recommendedIds = getMLRecommendations(interests);
            List<Gift> gifts = giftRepository.findAllById(recommendedIds);

            return new ApiResponse(gifts, true);
        } catch (Exception e) {
            log.error("Failed to get recommendations: ", e);
            return new ApiResponse("Error getting recommendations", false);
        }
    }

    private List<String> getUserInterests(Long userId) {
        // Реализуйте получение интересов пользователя
        return Collections.emptyList();
    }
}