package com.api.mediaapi.application.services;

import com.api.mediaapi.application.mappers.ImageResponseMapper;
import com.api.mediaapi.domain.dtos.image.*;
import com.api.mediaapi.domain.models.Image;
import com.api.mediaapi.infrastructure.azure.blobstorage.AzureBlobStorageService;
import com.api.mediaapi.infrastructure.persistence.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageResponseMapper imageResponseMapper;
    private final AzureBlobStorageService azureBlobStorageService;

    public ImageResponse getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        image.setUrl(azureBlobStorageService.getPublicImageUrl(image.getUrl()));

        return imageResponseMapper.apply(image);
    }

    public List<ImageResponse> getImagesByReferenceId(UUID referenceId) {
        List<Image> images = imageRepository.findAllByReferenceIdOrderByOrderIndexAsc(referenceId);

        images.forEach(img -> img.setUrl(azureBlobStorageService.getPublicImageUrl(img.getUrl())));

        return images.stream()
                .map(imageResponseMapper)
                .collect(Collectors.toList());
    }

    public Map<UUID, ImageResponse> getCoverImagesBatch(List<UUID> referenceIds) {
        List<Image> images = imageRepository.findAllByIsCoverAndReferenceIds(referenceIds);

        images.forEach(img -> img.setUrl(azureBlobStorageService.getPublicImageUrl(img.getUrl())));

        return images.stream()
                .collect(Collectors.toMap(
                        Image::getReferenceId,
                        imageResponseMapper));
    }

    @Transactional
    public List<ImageResponse> createImages(CreateImagesCommand request) {
        Integer maxIndex = imageRepository.findMaxOrderIndex(request.referenceId());
        AtomicInteger nextIndex = new AtomicInteger(maxIndex == null ? 1 : maxIndex + 1);

        List<Image> images = new ArrayList<>();

        request.images().forEach((ImagePayload image) -> {
            byte[] imageBytes = Base64.getDecoder().decode(image.base64Data());

            String url = azureBlobStorageService.uploadImage(imageBytes);

            Image newImage = Image.builder()
                    .fileName(image.fileName())
                    .url(url)
                    .referenceId(request.referenceId())
                    .orderIndex(nextIndex.getAndIncrement())
                    .build();

            Image savedImage = imageRepository.save(newImage);

            images.add(savedImage);
        });

        return images.stream()
                .peek(img -> img.setUrl(azureBlobStorageService.getPublicImageUrl(img.getUrl())))
                .map(imageResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteImage(DeleteImageCommand request) {
        Image image = imageRepository.findById(request.imageId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!image.getReferenceId().equals(request.referenceId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        azureBlobStorageService.deleteImage(image.getUrl());

        imageRepository.delete(image);

        List<Image> toShift = imageRepository.findAllByReferenceIdOrderByOrderIndexAsc(request.referenceId()).stream()
                .filter(i -> i.getOrderIndex() > image.getOrderIndex())
                .toList();

        for (Image i : toShift) {
            imageRepository.updateOrderIndex(i.getId(), request.referenceId(), i.getOrderIndex() - 1);
        }
    }

    @Transactional
    public List<ImageResponse> reorderImages(ReorderImagesCommand request) {
        List<Long> orderedIds = request.orderedImageIds();
        UUID referenceId = request.referenceId();

        List<Image> allImages = imageRepository.findAllByReferenceIdOrderByOrderIndexAsc(referenceId);
        Set<Long> existingIds = allImages.stream().map(Image::getId).collect(Collectors.toSet());
        Set<Long> orderedIdSet = new HashSet<>(orderedIds);

        if (!existingIds.equals(orderedIdSet)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (int i = 0; i < orderedIds.size(); i++) {
            Long imageId = orderedIds.get(i);
            imageRepository.updateOrderIndex(imageId, referenceId, -(i + 1));
        }

        for (int i = 0; i < orderedIds.size(); i++) {
            Long imageId = orderedIds.get(i);
            imageRepository.updateOrderIndex(imageId, referenceId, i + 1);
        }

        List<Image> images = imageRepository.findAllByReferenceIdOrderByOrderIndexAsc(referenceId);

        images.forEach(img -> img.setUrl(azureBlobStorageService.getPublicImageUrl(img.getUrl())));

        return images.stream()
                .map(imageResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteImagesByReferenceId(DeleteImagesByReferenceCommand request) {
        List<Image> images = imageRepository.findAllByReferenceIdOrderByOrderIndexAsc(request.referenceId());

        images.forEach(image -> {
            azureBlobStorageService.deleteImage(image.getUrl());
        });

        imageRepository.deleteAllByReferenceId(request.referenceId());
    }
}
