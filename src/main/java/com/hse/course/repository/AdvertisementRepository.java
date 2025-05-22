package com.hse.course.repository;

import com.hse.course.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findById(Long id);
    List<Advertisement> findByBrand(String brand);
    List<Advertisement> findByBrandContainingOrNameContaining(String Brand, String name);
}
