package com.api.mediaapi.application.services;

import com.api.mediaapi.api.producer.RabbitMQProducer;
import com.api.mediaapi.application.mappers.ImageResponseMapper;
import com.api.mediaapi.domain.dtos.image.CreateImageRequest;
import com.api.mediaapi.domain.dtos.image.DeleteImageRequest;
import com.api.mediaapi.domain.dtos.image.DeleteImagesRequest;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
import com.api.mediaapi.domain.models.Image;
import com.api.mediaapi.infrastructure.azure.blobstorage.AzureBlobStorageService;
import com.api.mediaapi.infrastructure.persistence.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final RabbitMQProducer rabbitMQProducer;

    public ImageResponse getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        image.setUrl(azureBlobStorageService.getPublicImageUrl(image.getUrl()));

        return imageResponseMapper.apply(image);
    }

    public List<ImageResponse> getImagesByReferenceId(UUID id) {
        List<Image> images = imageRepository.findAllByReferenceId(id);

        images.forEach(image -> {
            image.setUrl(azureBlobStorageService.getPublicImageUrl(image.getUrl()));
        });

        return images.stream()
                .map(imageResponseMapper)
                .collect(Collectors.toList());
    }

    public void createImage(CreateImageRequest request) {
        byte[] imageBytes = Base64.getDecoder().decode(request.getImageBase64());

        String url = azureBlobStorageService.uploadImage(imageBytes);

        Image newImage = Image.builder()
                .originalName(request.getImageName())
                .url(url)
                .referenceId(request.getReferenceId())
                .build();

        Image savedImage = imageRepository.save(newImage);

        rabbitMQProducer.sendMessageCreateImage(imageResponseMapper.apply(savedImage));
    }

    public void deleteImage(DeleteImageRequest request) {
        Image image = imageRepository.findById(request.getImageId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!image.getReferenceId().equals(request.getReferenceId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        azureBlobStorageService.deleteImage(image.getUrl());

        imageRepository.delete(image);

        rabbitMQProducer.sendMessageDeleteImage(imageResponseMapper.apply(image));
    }

    public void deleteImagesByReferenceId(DeleteImagesRequest request) {
        List<Image> images = imageRepository.findAllByReferenceId(request.getReferenceId());

        images.forEach(image -> {
            azureBlobStorageService.deleteImage(image.getUrl());

            imageRepository.delete(image);
        });

        rabbitMQProducer.sendMessageDeleteImages(images.stream()
                .map(imageResponseMapper)
                .collect(Collectors.toList()));
    }
}
