package com.hse.course.service;

import com.google.gson.Gson;
import com.hse.course.dto.MLResponse;
import com.hse.course.model.Gift;
import com.hse.course.model.GiftCollection;
import com.hse.course.model.User;
import com.hse.course.repository.CollectionRepository;
import com.hse.course.repository.GiftRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    private final Gson gson=new Gson();

    @Transactional
    public ApiResponse createCollection(String interestText, String title, User owner) {
        log.info("Creating collection for user: {}", owner.getEmail());
        GiftCollection collection = new GiftCollection();
        collection.setName(title);
        collection.setOwner(owner);
        collection.setDescription("На основе: " + interestText);
        log.info("Вызываем getMLRecommendations с интересами: {}", interestText);
        List<Long> recommendedIds = getMLRecommendations(interestText);
        log.info("Recommended IDs: {}", recommendedIds);
        collection.setAdvertisements(recommendedIds);
        return new ApiResponse(gson.toJson(collectionRepository.save(collection)),true);
    }

    public List<GiftCollection> getUserCollections(Long userId) {
        return collectionRepository.findByOwnerId(userId);
    }

    public List<Gift> getGiftsBySelectionId(Long selectionId) {
        GiftCollection giftCollection = collectionRepository.findById(selectionId)
                .orElseThrow(() -> new RuntimeException("GiftCollection is not found"));

        List<Gift> gifts = new ArrayList<>();
        for (long iden : giftCollection.getAdvertisements()) {
            gifts.add(giftRepository.findById(iden).get());
        }

        return gifts;
    }

    public Optional<GiftCollection> getCollection(Long collectionId) {
        return collectionRepository.findById(collectionId);
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

    public ApiResponse getRecommendedGifts(Long userId, String interest) {
        try {
            List<Long> recommendedIds = getMLRecommendations(interest);
            return new ApiResponse(recommendedIds, true);
        } catch (Exception e) {
            log.error("Failed to get recommendations: ", e);
            return new ApiResponse("Error getting recommendations", false);
        }
    }

    @Transactional
    public void deleteCollection(Long id) {

        collectionRepository.deleteById(id);
    }

    public List<GiftCollection> getRandomCollections(int count) {
        List<GiftCollection> allCollections = collectionRepository.findAll();
        Collections.shuffle(allCollections, new Random());
        return allCollections.stream()
                .limit(count)
                .toList();
    }


}