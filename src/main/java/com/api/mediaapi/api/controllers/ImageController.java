package com.api.mediaapi.api.controllers;

import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("reference/{referenceId}")
    public ResponseEntity<List<ImageResponse>> getImagesByReferenceId(@PathVariable UUID referenceId) {
        List<ImageResponse> response = imageService.getImagesByReferenceId(referenceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long id) {
        ImageResponse response = imageService.getImage(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("covers")
    public ResponseEntity<Map<UUID, ImageResponse>> getCoverImagesBatch(@RequestParam List<UUID> referenceIds) {
        Map<UUID, ImageResponse> response = imageService.getCoverImagesBatch(referenceIds);
        return ResponseEntity.ok(response);
    }
}
