package com.api.mediaapi.domain.dtos.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse implements Serializable {
    private Long id;
    private String url;
    private String fileName;
    private UUID referenceId;
    private Boolean isCover;
    private Integer orderIndex;
}
