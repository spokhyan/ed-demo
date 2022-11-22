package com.techbytes.ed.init.impl;

import com.techbytes.ed.config.KafkaDataConfig;
import com.techbytes.ed.init.StreamInitializer;
import com.techbytes.ed.kafka.admin.config.client.KafkaAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamInitializer implements StreamInitializer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamInitializer.class);

    private final KafkaDataConfig kafkaConfigData;

    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaDataConfig configData, KafkaAdminClient adminClient) {
        this.kafkaConfigData = configData;
        this.kafkaAdminClient = adminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopic();
        kafkaAdminClient.checkSchemaRegistry();
        logger.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopics().toArray());
    }
}
