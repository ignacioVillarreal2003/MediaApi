package com.api.mediaapi.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private String exchange;

    private Queues queue;
    private RoutingKeys routingKey;

    @Data
    public static class Queues {
        private String create;
        private String delete;
        private String deleteAll;
    }

    @Data
    public static class RoutingKeys {
        private String create;
        private String delete;
        private String deleteAll;
    }
}
