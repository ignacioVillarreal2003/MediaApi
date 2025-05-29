package com.api.mediaapi.domain.dtos.image;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CreateImageRequest {
    @Valid

    @NotNull(message = "Files is required")
    @Size(min = 1, message = "At least one file is required")
    public List<MultipartFile> files;
}
