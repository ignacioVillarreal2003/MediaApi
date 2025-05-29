package com.api.mediaapi.application.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MediaValidationHelper {

    public void verifyImages(List<MultipartFile> images) {
        List<MultipartFile> invalidFiles = images.stream()
                .filter(image -> !isValidImageType(image))
                .toList();
        if (!invalidFiles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidImageType(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/svg+xml"));
    }
}
