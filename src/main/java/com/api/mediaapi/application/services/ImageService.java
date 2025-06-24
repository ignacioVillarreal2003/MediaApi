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

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
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
        List<Image> images = imageRepository.findAllByReferenceId(referenceId);

        images.forEach(image -> {
            image.setUrl(azureBlobStorageService.getPublicImageUrl(image.getUrl()));
        });

        return images.stream()
                .map(imageResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Long> createImages(CreateImagesCommand request) {
        List<Long> createdImagesIds = new ArrayList<>();

        request.images().forEach((ImagePayload image) -> {
            byte[] imageBytes = Base64.getDecoder().decode(image.base64Data());

            String url = azureBlobStorageService.uploadImage(imageBytes);

            Image newImage = Image.builder()
                    .fileName(image.fileName())
                    .url(url)
                    .referenceId(request.referenceId())
                    .build();

            Image savedImage = imageRepository.save(newImage);

            createdImagesIds.add(savedImage.getId());
        });

        return createdImagesIds;
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
    }

    @Transactional
    public void deleteImagesByReferenceId(DeleteImagesByReferenceCommand request) {
        List<Image> images = imageRepository.findAllByReferenceId(request.referenceId());

        images.forEach(image -> {
            azureBlobStorageService.deleteImage(image.getUrl());
        });

        imageRepository.deleteAllByReferenceId(request.referenceId());
    }
}
