package com.api.mediaapi.config;

import com.api.mediaapi.config.properties.RabbitMQProperties;
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
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue createQueue() {
        return new Queue(rabbitMQProperties.getQueue().getCreate());
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(rabbitMQProperties.getQueue().getDelete());
    }

    @Bean
    public Queue deleteAllQueue() {
        return new Queue(rabbitMQProperties.getQueue().getDeleteAll());
    }

    @Bean
    public TopicExchange imageExchange() {
        return new TopicExchange(rabbitMQProperties.getExchange());
    }

    @Bean
    public Binding createBinding() {
        return BindingBuilder
                .bind(createQueue())
                .to(imageExchange())
                .with(rabbitMQProperties.getRoutingKey().getCreate());
    }

    @Bean
    public Binding deleteBinding() {
        return BindingBuilder
                .bind(deleteQueue())
                .to(imageExchange())
                .with(rabbitMQProperties.getRoutingKey().getDelete());
    }

    @Bean
    public Binding deleteAllBinding() {
        return BindingBuilder
                .bind(deleteAllQueue())
                .to(imageExchange())
                .with(rabbitMQProperties.getRoutingKey().getDeleteAll());
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
