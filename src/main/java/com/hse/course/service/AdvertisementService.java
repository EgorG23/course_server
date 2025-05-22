package com.hse.course.service;

import com.hse.course.dto.AdvertisementRequest;
import com.hse.course.exceptions.ResourceNotFoundException;
import com.hse.course.model.Advertisement;
import com.hse.course.model.User;
import com.hse.course.repository.AdvertisementRepository;
import com.hse.course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository repository;

    public Advertisement createAdvertisement(Advertisement ad) {
        return repository.save(ad);
    }

    public Optional<Advertisement> getAdvertisementById(Long id) {
        return repository.findById(id);
    }

    public List<Advertisement> getAllAdvertisements() {
        return repository.findAll();
    }

    public List<Advertisement> searchAdvertisements(String query) {
        return repository.findByBrandContainingOrNameContaining(query, query);
    }

}