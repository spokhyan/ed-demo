package com.techbytes.ed.kafka.admin.config;

import com.techbytes.ed.config.KafkaDataConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@EnableRetry
@Configuration
public class KafkaAdminConfig {
    private final KafkaDataConfig kafkaDataConfig;


    public KafkaAdminConfig(KafkaDataConfig kafkaDataConfig) {
        this.kafkaDataConfig = kafkaDataConfig;
    }

    @Bean
    public AdminClient adminClient (){
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                kafkaDataConfig.getBootStrapServer()));
    }
}
