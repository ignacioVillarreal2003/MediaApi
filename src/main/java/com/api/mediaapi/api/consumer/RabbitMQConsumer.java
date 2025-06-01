package com.api.mediaapi.api.consumer;

import com.api.mediaapi.application.services.ImageService;
import com.api.mediaapi.domain.dtos.image.CreateImageRequest;
import com.api.mediaapi.domain.dtos.image.DeleteImageRequest;
import com.api.mediaapi.domain.dtos.image.DeleteImagesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final ImageService imageService;

    @RabbitListener(queues = "${rabbitmq.queue.create}")
    public void receiveMessageCreateImage(CreateImageRequest message) {
        imageService.createImage(message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.delete}")
    public void receiveMessageDeleteImage(DeleteImageRequest message) {
        imageService.deleteImage(message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.deleteAll}")
    public void receiveMessageDeleteImages(DeleteImagesRequest message) {
        imageService.deleteImagesByReferenceId(message);
    }
}
