package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateImagesCommand (
        UUID sagaId,
        UUID referenceId,
        List<ImagePayload> images
) {
}
