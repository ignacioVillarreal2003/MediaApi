package com.api.mediaapi.application.services;

import com.api.mediaapi.application.helpers.MediaValidationHelper;
import com.api.mediaapi.application.mappers.ImageResponseMapper;
import com.api.mediaapi.domain.dtos.image.CreateImageRequest;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
import com.api.mediaapi.domain.models.Image;
import com.api.mediaapi.infrastructure.azure.blobstorage.AzureBlobStorageService;
import com.api.mediaapi.infrastructure.persistence.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageResponseMapper imageResponseMapper;
    private final AzureBlobStorageService azureBlobStorageService;
    private final MediaValidationHelper mediaValidationHelper;

    public ImageResponse getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        image.setUrl(azureBlobStorageService.getPublicImageUrl(image.getUrl()));

        return imageResponseMapper.apply(image);
    }

    public List<ImageResponse> createImage(CreateImageRequest request) {
        mediaValidationHelper.verifyImages(request.files);

        List<Image> images = new ArrayList<>();

        request.files.forEach((file -> {
            String url = azureBlobStorageService.uploadImage(file);

            Image newImage = Image.builder()
                    .originalName(file.getOriginalFilename())
                    .url(url)
                    .build();

            imageRepository.save(newImage);

            images.add(newImage);
        }));

        return images.stream()
                .map(imageResponseMapper)
                .collect(Collectors.toList());
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        azureBlobStorageService.deleteImage(image.getUrl());

        imageRepository.delete(image);
    }
}
