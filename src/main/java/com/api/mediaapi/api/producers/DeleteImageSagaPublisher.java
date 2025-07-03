package com.api.mediaapi.api.producers;

import com.api.mediaapi.config.properties.RabbitProperties;
import com.api.mediaapi.domain.dtos.image.DeleteImageReply;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteImageSagaPublisher {

    private final RabbitProperties rabbitProperties;
    private final RabbitTemplate rabbitTemplate;

    public void publishDeleteImageReply(DeleteImageReply message) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getMedia(),
                rabbitProperties.getRoutingKey().getDeleteImageReply(),
                message
        );
    }
}
