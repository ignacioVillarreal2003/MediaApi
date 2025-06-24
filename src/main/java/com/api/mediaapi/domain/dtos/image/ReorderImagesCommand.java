package com.api.mediaapi.domain.dtos.image;

import java.util.List;
import java.util.UUID;

public record ReorderImagesCommand (
        UUID sagaId,
        UUID referenceId,
        List<Long> orderedImageIds
) {
}
