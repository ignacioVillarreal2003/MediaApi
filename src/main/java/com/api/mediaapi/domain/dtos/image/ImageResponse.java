package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class ImageResponse implements Serializable {
    private Long id;
    private String originalName;
    private String url;
    private UUID referenceId;
}
