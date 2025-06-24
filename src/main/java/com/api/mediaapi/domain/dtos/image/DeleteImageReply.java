package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public class DeleteImageReply implements Serializable {
        private UUID sagaId;
        private boolean success;
        private String errorMessage;
}
