package com.api.mediaapi.infrastructure.persistence.repositories;

import com.api.mediaapi.domain.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("""
        select i from Image i
        where i.referenceId = :referenceId
    """)
    List<Image> findAllByReferenceId(UUID referenceId);
}
