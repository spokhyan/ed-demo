package com.techbytes.ed.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-stream")
public class KafkaStreamsServiceConfigData {
	private String version;
	private String customAudience;
}
