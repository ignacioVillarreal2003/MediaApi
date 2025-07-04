package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteImagesByReferenceCommand(
    UUID sagaId,
    UUID referenceId
) {
}