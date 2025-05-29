package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class ImageResponse implements Serializable {
    private Long id;
    private String originalName;
    private String url;
    private Long userId;
}
