package com.api.mediaapi.infrastructure.persistence.repositories;

import com.api.mediaapi.domain.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByReferenceIdOrderByOrderIndexAsc(UUID referenceId);

    @Query("SELECT max(i.orderIndex) FROM Image i WHERE i.referenceId = :referenceId")
    Integer findMaxOrderIndex(@Param("referenceId") UUID referenceId);

    @Modifying
    @Query("UPDATE Image i SET i.orderIndex = :newIndex WHERE i.id = :id AND i.referenceId = :referenceId")
    void updateOrderIndex(@Param("id") Long id,
                          @Param("referenceId") UUID referenceId,
                          @Param("newIndex") Integer newIndex);

    void deleteAllByReferenceId(UUID referenceId);

    @Query("SELECT i FROM Image i WHERE i.referenceId IN :referenceIds AND i.orderIndex = 1")
    List<Image> findAllByIsCoverAndReferenceIds(@Param("referenceIds") List<UUID> referenceIds);
}
