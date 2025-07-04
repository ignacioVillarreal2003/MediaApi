package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.constraints.NotNull;

public record ImagePayload (
        String fileName,
        String base64Data,
        Integer orderIndex
) {
}
