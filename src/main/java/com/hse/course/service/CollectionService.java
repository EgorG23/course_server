package com.hse.course.service;

import com.hse.course.dto.CreateCollectionRequest;
import com.hse.course.dto.UpdateCollectionRequest;
import com.hse.course.model.Gift;
import com.hse.course.model.GiftCollection;
import com.hse.course.model.User;
import com.hse.course.repository.CollectionRepository;
import com.hse.course.repository.GiftRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private CollectionRepository collectionRepository;
    private GiftRepository giftRepository;
    private RestTemplate restTemplate;
    private UserRepository userRepository;
    private User user;

    public GiftCollection createCollection(CreateCollectionRequest request, User owner) {
        GiftCollection collection = new GiftCollection();
        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        collection.setInterests(request.getInterests());
        collection.setOwner(owner);

        List<Long> recommendedGiftIds = getMlRecommendations(request.getInterests());
        List<Gift> recommendedGifts = giftRepository.findAllById(recommendedGiftIds);
        collection.setGifts(recommendedGifts);

        return collectionRepository.save(collection);
    }

    private List<Long> getMlRecommendations(List<String> interests) {
        String mlServiceUrl = "http://localhost:8000/recommend";
        return restTemplate.postForObject(
                mlServiceUrl,
                Map.of("interests", interests),
                List.class
        );
    }

    public List<GiftCollection> getUserCollections(Long userId) {
        return collectionRepository.findByOwnerId(userId);
    }

    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }

    public GiftCollection updateCollection(Long id, UpdateCollectionRequest request) {
        GiftCollection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (request.getName() != null) collection.setName(request.getName());
        if (request.getDescription() != null) collection.setDescription(request.getDescription());
        if (request.getInterests() != null) {
            collection.setInterests(request.getInterests());
            List<Long> newRecommendations = getMlRecommendations(request.getInterests());
            collection.setGifts(giftRepository.findAllById(newRecommendations));
        }

        return collectionRepository.save(collection);
    }

    public ApiResponse getRecommendedGifts(Long userId, String interest) {
        // 1. Получаем пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Получаем интересы пользователя (или используем переданный интерес)
        Object interests = (interest != null)
                ? Set.of(Integer.parseInt(interest))
                : user.getInterest();

        // 3. Получаем рекомендации через ML-сервис
        List<Long> recommendedGiftIds = mlService.getRecommendations(interests);

        // 4. Получаем полные данные о подарках
        List<Gift> gifts = giftRepository.findAllById(recommendedGiftIds);

        return new ApiResponse(gifts, true);
    }
}
