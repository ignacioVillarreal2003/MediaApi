package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.constraints.NotNull;

public record ImagePayload (
        @NotNull(message = "File name is required")
        String fileName,

        @NotNull(message = "Base 64 data is required")
        String base64Data
) {
}
