package com.api.mediaapi.application.mappers;

import com.api.mediaapi.domain.dtos.image.ImageResponse;
import com.api.mediaapi.domain.models.Image;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ImageResponseMapper implements Function<Image, ImageResponse> {

    @Override
    public ImageResponse apply(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .originalName(image.getOriginalName())
                .url(image.getUrl())
                .referenceId(image.getReferenceId())
                .build();
    }
}
