package com.techbytes.ed.kafka.stream.init.impl;


import com.techbytes.ed.config.KafkaDataConfig;
import com.techbytes.ed.kafka.admin.config.client.KafkaAdminClient;
import com.techbytes.ed.kafka.stream.init.StreamsInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamsInitializer implements StreamsInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsInitializer.class);

    private final KafkaDataConfig kafkaConfigData;

    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamsInitializer(KafkaDataConfig configData, KafkaAdminClient adminClient) {
        this.kafkaConfigData = configData;
        this.kafkaAdminClient = adminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.checkTopicsCreated();
        kafkaAdminClient.checkSchemaRegistry();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopics().toArray());
    }
}
