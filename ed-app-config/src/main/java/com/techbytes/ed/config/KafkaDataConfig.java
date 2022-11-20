package com.techbytes.ed.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaDataConfig {

    private String bootStrapServer;
    private String schemaRegistryUriKey;
    private String schemaRegistryUri;
    private String topicName;
    private List<String> topics;
    private Integer partitionCount;
    private Short replicationFactor;


}
