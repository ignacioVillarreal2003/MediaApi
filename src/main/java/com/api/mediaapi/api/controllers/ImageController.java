package com.api.mediaapi.api.controllers;

import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.CreateImageRequest;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long id) {
        ImageResponse response = imageService.getImage(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<List<ImageResponse>> createImage(@RequestBody CreateImageRequest request) {
        List<ImageResponse> response = imageService.createImage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
