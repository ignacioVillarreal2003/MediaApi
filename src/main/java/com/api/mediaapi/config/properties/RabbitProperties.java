package com.api.mediaapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Getter
@Setter
public class RabbitProperties {

    private Exchange exchange;
    private Queues queue;
    private RoutingKeys routingKey;

    @Getter
    @Setter
    public static class Exchange {
        private String media;
    }

    @Getter
    @Setter
    public static class Queues {
        private String createImagesCommand;
        private String reorderImagesCommand;
        private String deleteImageCommand;
        private String deleteImagesByReferenceCommand;
        private String createImagesReply;
        private String reorderImagesReply;
        private String deleteImageReply;
        private String deleteImagesByReferenceReply;
    }

    @Getter
    @Setter
    public static class RoutingKeys {
        private String createImagesCommand;
        private String reorderImagesCommand;
        private String deleteImageCommand;
        private String deleteImagesByReferenceCommand;
        private String createImagesReply;
        private String reorderImagesReply;
        private String deleteImageReply;
        private String deleteImagesByReferenceReply;
    }
}
