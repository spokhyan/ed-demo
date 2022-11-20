package com.techbytes.ed.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {

    private Long initialIntervalMi;
    private Long maxIntervalMi;
    private Double multiplier;
    private Integer maxRetry;
    private Long sleepTimeMi;


}
