package com.api.mediaapi.api.consumers;

import com.api.mediaapi.api.producers.ReorderImagesSagaPublisher;
import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReorderImagesSagaConsumer {
    private final ImageService imageService;
    private final ReorderImagesSagaPublisher reorderImagesSagaPublisher;

    @RabbitListener(queues = "${rabbitmq.queue.reorder-images-command}")
    public void handleReorderImagesCommand(@Valid @Payload ReorderImagesCommand message) {
        UUID sagaId = message.sagaId();
        try {
            List<ImageResponse> images = imageService.reorderImages(message);

            ReorderImagesReply response = ReorderImagesReply.builder()
                    .sagaId(sagaId)
                    .success(true)
                    .images(images)
                    .build();

            reorderImagesSagaPublisher.publishReorderImagesReply(response);
        }
        catch (ResponseStatusException ex) {
            ReorderImagesReply response = ReorderImagesReply.builder()
                    .sagaId(sagaId)
                    .success(false)
                    .errorMessage("[" + ex.getStatusCode().value() + "] " + ex.getReason())
                    .build();
            reorderImagesSagaPublisher.publishReorderImagesReply(response);
        }
        catch (Exception ex) {
            if (ex.getCause() instanceof MethodArgumentNotValidException validationEx) {
                StringBuilder errors = new StringBuilder();
                validationEx.getBindingResult().getFieldErrors().forEach(fe ->
                        errors.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("; ")
                );
                ReorderImagesReply response = ReorderImagesReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[400] " + errors)
                        .build();
                reorderImagesSagaPublisher.publishReorderImagesReply(response);
            }
            else {
                ReorderImagesReply response = ReorderImagesReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[500] " + ex.getMessage())
                        .build();
                reorderImagesSagaPublisher.publishReorderImagesReply(response);
            }
        }
    }
}
