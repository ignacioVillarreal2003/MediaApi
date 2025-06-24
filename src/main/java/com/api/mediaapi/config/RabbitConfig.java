package com.api.mediaapi.config;

import com.api.mediaapi.config.properties.RabbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperties rabbitMQProperties;

    @Bean
    public TopicExchange imageExchange() {
        return new TopicExchange(rabbitMQProperties.getExchange().getImage());
    }

    @Bean
    public Queue createImagesCommandQueue() {
        return new Queue(rabbitMQProperties.getQueue().getCreateImagesCommand());
    }

    @Bean
    public Queue deleteImageCommandQueue() {
        return new Queue(rabbitMQProperties.getQueue().getDeleteImageCommand());
    }

    @Bean
    public Queue deleteImagesByReferenceCommandQueue() {
        return new Queue(rabbitMQProperties.getQueue().getDeleteImagesByReferenceCommand());
    }

    @Bean
    public Binding bindingCreateImagesCommand() {
        return BindingBuilder
                .bind(createImagesCommandQueue())
                .to(imageExchange())
                .with(rabbitMQProperties.getRoutingKey().getCreateImagesCommand());
    }

    @Bean
    public Binding bindingDeleteImageCommand() {
        return BindingBuilder
                .bind(deleteImageCommandQueue())
                .to(imageExchange())
                .with(rabbitMQProperties.getRoutingKey().getDeleteImageCommand());
    }

    @Bean
    public Binding bindingDeleteImagesByReferenceCommand() {
        return BindingBuilder
                .bind(deleteImagesByReferenceCommandQueue())
                .to(imageExchange())
                .with(rabbitMQProperties.getRoutingKey().getDeleteImagesByReferenceCommand());
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
