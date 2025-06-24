package com.api.mediaapi.infrastructure.persistence.repositories;

import com.api.mediaapi.domain.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByReferenceId(UUID referenceId);
    void deleteAllByReferenceId(UUID referenceId);
}
