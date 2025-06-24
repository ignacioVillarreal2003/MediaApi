package com.api.mediaapi.api.consumers;

import com.api.mediaapi.api.producers.CreateImagesSagaPublisher;
import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.CreateImagesCommand;
import com.api.mediaapi.domain.dtos.image.CreateImagesReply;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
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
public class CreateImagesSagaConsumer {
    private final ImageService imageService;
    private final CreateImagesSagaPublisher createImagesSagaPublisher;

    @RabbitListener(queues = "${rabbitmq.queue.create-images-command}")
    public void handleCreateImagesCommand(@Valid @Payload CreateImagesCommand message) {
        UUID sagaId = message.sagaId();
        try {
            List<ImageResponse> images = imageService.createImages(message);

            CreateImagesReply response = CreateImagesReply.builder()
                    .sagaId(sagaId)
                    .success(true)
                    .images(images)
                    .build();

            createImagesSagaPublisher.publishCreateImagesReply(response);
        }
        catch (ResponseStatusException ex) {
            CreateImagesReply response = CreateImagesReply.builder()
                    .sagaId(sagaId)
                    .success(false)
                    .errorMessage("[" + ex.getStatusCode().value() + "] " + ex.getReason())
                    .build();
            createImagesSagaPublisher.publishCreateImagesReply(response);
        }
        catch (Exception ex) {
            if (ex.getCause() instanceof MethodArgumentNotValidException validationEx) {
                StringBuilder errors = new StringBuilder();
                validationEx.getBindingResult().getFieldErrors().forEach(fe ->
                        errors.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("; ")
                );
                CreateImagesReply response = CreateImagesReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[400] " + errors)
                        .build();
                createImagesSagaPublisher.publishCreateImagesReply(response);
            }
            else {
                CreateImagesReply response = CreateImagesReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[500] " + ex.getMessage())
                        .build();
                createImagesSagaPublisher.publishCreateImagesReply(response);
            }
        }
    }
}
