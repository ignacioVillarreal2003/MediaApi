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

    private final RabbitProperties rabbitProperties;

    @Bean
    public TopicExchange mediaExchange() {
        return new TopicExchange(rabbitProperties.getExchange().getMedia());
    }

    @Bean
    public Queue createImagesCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getCreateImagesCommand());
    }

    @Bean
    public Queue reorderImagesCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getReorderImagesCommand());
    }

    @Bean
    public Queue deleteImageCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getDeleteImageCommand());
    }

    @Bean
    public Queue deleteImagesByReferenceCommandQueue() {
        return new Queue(rabbitProperties.getQueue().getDeleteImagesByReferenceCommand());
    }

    @Bean
    public Queue createImagesReplyQueue() {
        return new Queue(rabbitProperties.getQueue().getCreateImagesReply());
    }

    @Bean
    public Queue reorderImagesReplyQueue() {
        return new Queue(rabbitProperties.getQueue().getReorderImagesReply());
    }

    @Bean
    public Queue deleteImageReplyQueue() {
        return new Queue(rabbitProperties.getQueue().getDeleteImageReply());
    }

    @Bean
    public Queue deleteImagesByReferenceReplyQueue() {
        return new Queue(rabbitProperties.getQueue().getDeleteImagesByReferenceReply());
    }

    @Bean
    public Binding bindingCreateImagesCommand() {
        return BindingBuilder
                .bind(createImagesCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getCreateImagesCommand());
    }

    @Bean
    public Binding bindingReorderImagesCommand() {
        return BindingBuilder
                .bind(reorderImagesCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getReorderImagesCommand());
    }

    @Bean
    public Binding bindingDeleteImageCommand() {
        return BindingBuilder
                .bind(deleteImageCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getDeleteImageCommand());
    }

    @Bean
    public Binding bindingDeleteImagesByReferenceCommand() {
        return BindingBuilder
                .bind(deleteImagesByReferenceCommandQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getDeleteImagesByReferenceCommand());
    }

    @Bean
    public Binding bindingCreateImagesReply() {
        return BindingBuilder
                .bind(createImagesReplyQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getCreateImagesReply());
    }

    @Bean
    public Binding bindingReorderImagesReply() {
        return BindingBuilder
                .bind(reorderImagesReplyQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getReorderImagesReply());
    }

    @Bean
    public Binding bindingDeleteImageReply() {
        return BindingBuilder
                .bind(deleteImageReplyQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getDeleteImageReply());
    }

    @Bean
    public Binding bindingDeleteImagesByReferenceReply() {
        return BindingBuilder
                .bind(deleteImagesByReferenceReplyQueue())
                .to(mediaExchange())
                .with(rabbitProperties.getRoutingKey().getDeleteImagesByReferenceReply());
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
