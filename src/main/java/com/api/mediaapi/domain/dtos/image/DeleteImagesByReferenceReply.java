package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public class DeleteImagesByReferenceReply implements Serializable {
        private UUID sagaId;
        private UUID referenceId;
        private boolean success;
        private String errorMessage;
}
