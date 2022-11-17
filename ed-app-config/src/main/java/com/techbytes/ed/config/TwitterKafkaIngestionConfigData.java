package com.techbytes.ed.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-kafka-ingestion")
public class TwitterKafkaIngestionConfigData {
    public List<String> twitterKeywords;
    public String greetingMessage;

    //add twitter v2 config params

    public String twitterV2BaseUrl;
    public String twitterV2RulesBaseUrl;
    public String twitterV2BearerToken;


}
