package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateImagesReply implements Serializable {
        private UUID sagaId;
        private List<ImageResponse> images;
        private boolean success;
        private String errorMessage;
}
