package com.api.mediaapi.infrastructure.persistence.repositories;

import com.api.mediaapi.domain.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
