package com.api.mediaapi.api.producer;

import com.api.mediaapi.config.properties.RabbitMQProperties;
import com.api.mediaapi.domain.dtos.image.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private RabbitMQProperties rabbitMQProperties;
    private RabbitTemplate rabbitTemplate;

    public void sendMessageCreateImage(ImageResponse message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange(),
                rabbitMQProperties.getRoutingKey().getCreate(),
                message
        );
    }

    public void sendMessageDeleteImage(ImageResponse message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange(),
                rabbitMQProperties.getRoutingKey().getDelete(),
                message
        );
    }

    public void sendMessageDeleteImages(List<ImageResponse> message) {
        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange(),
                rabbitMQProperties.getRoutingKey().getDeleteAll(),
                message
        );
    }
}
