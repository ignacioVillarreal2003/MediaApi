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
public class DeleteImageReply implements Serializable {
        private UUID sagaId;
        private boolean success;
        private String errorMessage;
}
