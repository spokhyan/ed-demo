package com.techbytes.ed.listener;

import com.techbytes.ed.config.KafkaDataConfig;
import com.techbytes.ed.kafka.avro.model.TwitterAvroModel;
import com.techbytes.ed.kafka.producer.service.KafkaProducer;
import com.techbytes.ed.util.TwitterStatusToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

    private final KafkaDataConfig kafkaConfigData;

    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;

    private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;

    public TwitterKafkaStatusListener(KafkaDataConfig kafkaConfigData, KafkaProducer<Long, TwitterAvroModel> kafkaProducer, TwitterStatusToAvroTransformer twitterStatusToAvroTransformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.twitterStatusToAvroTransformer = twitterStatusToAvroTransformer;
    }


    @Override
    public void onStatus(Status status) {

        logger.info("Received status text {} sending to kafka topic {}", status.getText(),
                kafkaConfigData.getTopicName());

        TwitterAvroModel twitterAvroModel
                = twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status);
        kafkaProducer
                .send(
                        kafkaConfigData.getTopicName(),
                        twitterAvroModel.getUserId(),
                        twitterAvroModel);

    }
}
