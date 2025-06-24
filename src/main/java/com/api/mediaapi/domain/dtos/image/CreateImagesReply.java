package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public class CreateImagesReply implements Serializable {
        private UUID sagaId;
        private UUID referenceId;
        private List<Long> imagesIds;
        private boolean success;
        private String errorMessage;
}
