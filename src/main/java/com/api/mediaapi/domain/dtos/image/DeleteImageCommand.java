package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteImageCommand (
    @NotNull(message = "Saga id is required")
    UUID sagaId,

    @NotNull(message = "Reference id is required")
    UUID referenceId,

    @NotNull(message = "Image id is required")
    Long imageId
) {
}