package com.api.mediaapi.api.producers;

import com.api.mediaapi.config.properties.RabbitProperties;
import com.api.mediaapi.domain.dtos.image.CreateImagesReply;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateImagesSagaPublisher {

    private final RabbitProperties rabbitProperties;
    private final RabbitTemplate rabbitTemplate;

    public void publishCreateImagesReply(CreateImagesReply message) {
        rabbitTemplate.convertAndSend(
                rabbitProperties.getExchange().getImage(),
                rabbitProperties.getRoutingKey().getCreateImagesCommand(),
                message
        );
    }
}
