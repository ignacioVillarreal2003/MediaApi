package com.api.mediaapi.api.consumers;

import com.api.mediaapi.api.producers.DeleteImagesByReferenceSagaPublisher;
import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.DeleteImagesByReferenceCommand;
import com.api.mediaapi.domain.dtos.image.DeleteImagesByReferenceReply;
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
public class DeleteImagesByReferenceSagaConsumer {

    private final ImageService imageService;
    private final DeleteImagesByReferenceSagaPublisher deleteImagesByReferenceSagaPublisher;

    @RabbitListener(queues = "${rabbitmq.queue.delete-images-by-reference-command}")
    public void handleDeleteImagesByReferenceCommand(@Valid @Payload DeleteImagesByReferenceCommand message) {
        UUID sagaId = message.sagaId();
        try {
            imageService.deleteImagesByReferenceId(message);

            DeleteImagesByReferenceReply response = DeleteImagesByReferenceReply.builder()
                    .sagaId(sagaId)
                    .success(true)
                    .referenceId(message.referenceId())
                    .build();

            deleteImagesByReferenceSagaPublisher.publishDeleteImagesByReferenceReply(response);
        }
        catch (ResponseStatusException ex) {
            DeleteImagesByReferenceReply response = DeleteImagesByReferenceReply.builder()
                    .sagaId(sagaId)
                    .success(false)
                    .errorMessage("[" + ex.getStatusCode().value() + "] " + ex.getReason())
                    .build();
            deleteImagesByReferenceSagaPublisher.publishDeleteImagesByReferenceReply(response);
        }
        catch (Exception ex) {
            if (ex.getCause() instanceof MethodArgumentNotValidException validationEx) {
                StringBuilder errors = new StringBuilder();
                validationEx.getBindingResult().getFieldErrors().forEach(fe ->
                        errors.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("; ")
                );
                DeleteImagesByReferenceReply response = DeleteImagesByReferenceReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[400] " + errors)
                        .build();
                deleteImagesByReferenceSagaPublisher.publishDeleteImagesByReferenceReply(response);
            }
            else {
                DeleteImagesByReferenceReply response = DeleteImagesByReferenceReply.builder()
                        .sagaId(sagaId)
                        .success(false)
                        .errorMessage("[500] " + ex.getMessage())
                        .build();
                deleteImagesByReferenceSagaPublisher.publishDeleteImagesByReferenceReply(response);
            }
        }
    }
}
