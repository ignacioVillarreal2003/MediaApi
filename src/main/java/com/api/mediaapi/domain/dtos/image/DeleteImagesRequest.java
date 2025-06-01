package com.api.mediaapi.domain.dtos.image;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class DeleteImagesRequest implements Serializable {
    private UUID referenceId;
}