package com.techbytes.ed.kafka.producer.config;

import com.techbytes.ed.config.KafkaDataConfig;
import com.techbytes.ed.config.KafkaProducerDataConfig;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {
    private final KafkaDataConfig kafkaDataConfig;

    private final KafkaProducerDataConfig kafkaProducerDataConfig;


    public KafkaProducerConfig(KafkaDataConfig kafkaDataConfig,
                               KafkaProducerDataConfig kafkaProducerDataConfig) {
        this.kafkaDataConfig = kafkaDataConfig;
        this.kafkaProducerDataConfig = kafkaProducerDataConfig;
    }

    private Map<String, Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaDataConfig.getBootStrapServer());
        props.put(kafkaDataConfig.getSchemaRegistryUriKey(), kafkaDataConfig.getSchemaRegistryUri());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerDataConfig.getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerDataConfig.getValueSerializerClass());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerDataConfig.getBatchSize() *
                kafkaProducerDataConfig.getBatchSizeBoostFactor());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerDataConfig.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerDataConfig.getCompressionType());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerDataConfig.getAck());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerDataConfig.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerDataConfig.getRetryCount());

        return props;
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
