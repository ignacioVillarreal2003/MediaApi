package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public class ReorderImagesReply implements Serializable {
        private UUID sagaId;
        private List<ImageResponse> images;
        private boolean success;
        private String errorMessage;
}
