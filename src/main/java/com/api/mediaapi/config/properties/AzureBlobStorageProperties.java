package com.api.mediaapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure.blob-storage")
@Getter
@Setter
public class AzureBlobStorageProperties {
    private String connectionString;
    private String containerName;
}
