package com.techbytes.ed.kafka.elastic.consumer.impl;

import com.techbytes.ed.config.KafkaConsumerConfigData;
import com.techbytes.ed.config.KafkaDataConfig;
import com.techbytes.ed.elastic.index.client.service.ElasticIndexClient;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import com.techbytes.ed.kafka.admin.config.client.KafkaAdminClient;
import com.techbytes.ed.kafka.avro.model.TwitterAvroModel;
import com.techbytes.ed.kafka.elastic.consumer.KafkaConsumer;
import com.techbytes.ed.kafka.elastic.transformer.AvroToElasticModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class KafkaConsumerImpl implements KafkaConsumer<Long, TwitterAvroModel> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaDataConfig kafkaConfigData;

    private final AvroToElasticModelTransformer avroToElasticModelTransformer;
    private final ElasticIndexClient<TwitterIndexModelImpl>  elasticIndexClient;

    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    public KafkaConsumerImpl(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                             KafkaAdminClient kafkaAdminClient,
                             KafkaDataConfig kafkaConfigData,
                             AvroToElasticModelTransformer avroToElasticModelTransformer,
                             ElasticIndexClient<TwitterIndexModelImpl> elasticIndexClient,
                             KafkaConsumerConfigData kafkaConsumerConfigData) {
        logger.info("inside KafkaConsumerImpl () ...");
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;

        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        logger.info("inside KafkaConsumerImpl () exit ...");
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        logger.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopics().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry
                .getListenerContainer("twitterTopicListener")).start();
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

        List<TwitterIndexModelImpl> elasticModels =
                avroToElasticModelTransformer.getElasticModels(messages);
        List<String> documentIds = elasticIndexClient.save(elasticModels);

        logger.info("Documents saved to elasticsearch with ids {}", documentIds.toArray());

    }
}
