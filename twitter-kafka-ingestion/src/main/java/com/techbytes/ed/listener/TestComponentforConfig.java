package com.techbytes.ed.listener;

import com.techbytes.ed.config.TwitterKafkaIngestionConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestComponentforConfig {
    private static final Logger logger = LoggerFactory.getLogger(TestComponentforConfig.class);
    private final TwitterKafkaIngestionConfigData twitterConfigData;


    public TestComponentforConfig(TwitterKafkaIngestionConfigData twitterConfigData) {
        this.twitterConfigData = twitterConfigData;
    }

    public void printConfigData(){
        logger.info("twitterConfigData " + twitterConfigData.getTwitterV2BearerToken());
    }
}
