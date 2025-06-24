package com.api.mediaapi.api.controllers;

import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("reference/{id}")
    public ResponseEntity<List<ImageResponse>> getImages(@PathVariable UUID id) {
        List<ImageResponse> response = imageService.getImagesByReferenceId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long id) {
        ImageResponse response = imageService.getImage(id);
        return ResponseEntity.ok(response);
    }
}
