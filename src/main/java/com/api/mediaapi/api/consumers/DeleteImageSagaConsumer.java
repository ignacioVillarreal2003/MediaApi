package com.api.mediaapi.api.consumers;

import com.api.mediaapi.api.producers.DeleteImageSagaPublisher;
import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteImageSagaConsumer {

    private final ImageService imageService;
    private final DeleteImageSagaPublisher deleteImageSagaPublisher;

    @RabbitListener(queues = "${rabbitmq.queue.delete-image-command}")
    public void handleDeleteImageCommand(@Valid @Payload DeleteImageCommand message) {
        UUID sagaId = message.sagaId();
        try {
            imageService.deleteImage(message);

            DeleteImageReply response = DeleteImageReply.builder()
                    .sagaId(sagaId)
                    .success(true)
                    .referenceId(message.referenceId())
                    .imageId(message.imageId())
                    .build();

            deleteImageSagaPublisher.publishDeleteImageReply(response);
        }
        catch (ResponseStatusException ex) {
            DeleteImageReply response = DeleteImageReply.builder()
                    .sagaId(sagaId)
                    .success(false)
                    .errorMessage("[" + ex.getStatusCode().value() + "] " + ex.getReason())
                    .build();
            deleteImageSagaPublisher.publishDeleteImageReply(response);
        }
        catch (Exception ex) {
            if (ex.getCause() instanceof MethodArgumentNotValidException validationEx) {
                StringBuilder errors = new StringBuilder();
                validationEx.getBindingResult().getFieldErrors().forEach(fe ->
                        errors.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("; ")
                );
                DeleteImageReply response = DeleteImageReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[400] " + errors)
                        .build();
                deleteImageSagaPublisher.publishDeleteImageReply(response);
            }
            else {
                DeleteImageReply response = DeleteImageReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[500] " + ex.getMessage())
                        .build();
                deleteImageSagaPublisher.publishDeleteImageReply(response);
            }
        }
    }
}
