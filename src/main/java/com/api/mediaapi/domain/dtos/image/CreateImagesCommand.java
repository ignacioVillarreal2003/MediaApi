package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateImagesCommand (
        @NotNull(message = "Saga id is required")
        UUID sagaId,

        @NotNull(message = "Reference id is required")
        UUID referenceId,

        @NotNull(message = "Images is required")
        List<ImagePayload> images
) {
}
