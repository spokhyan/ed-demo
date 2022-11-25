package com.techbytes.ed.kafka.elastic.consumer.impl;

import com.techbytes.ed.config.KafkaDataConfig;
import com.techbytes.ed.kafka.admin.config.client.KafkaAdminClient;
import com.techbytes.ed.kafka.avro.model.TwitterAvroModel;
import com.techbytes.ed.kafka.elastic.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

public class KafkaConsumerImpl implements KafkaConsumer<Long, TwitterAvroModel> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaDataConfig kafkaConfigData;

    public KafkaConsumerImpl(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaAdminClient kafkaAdminClient, KafkaDataConfig kafkaConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        logger.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopics().toArray());
        kafkaListenerEndpointRegistry.getListenerContainer("twitterTopicListener").start();
    }


    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List partitions,
                        @Header(KafkaHeaders.OFFSET) List offsets) {
        logger.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

    }
}
