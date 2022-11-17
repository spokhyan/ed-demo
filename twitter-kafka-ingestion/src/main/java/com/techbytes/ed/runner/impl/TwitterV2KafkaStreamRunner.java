package com.techbytes.ed.runner.impl;

import com.techbytes.ed.config.TwitterKafkaIngestionConfigData;
import com.techbytes.ed.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
//@ConditionalOnProperty(name = "twitter-kafka-ingestion.enable-v2-tweets")
//@ConditionalOnExpression("${twitter-kafka-ingestion.enable-v2-tweets} && not ${twitter-kafka-ingestion.enable-mock-tweets}")
public class TwitterV2KafkaStreamRunner implements StreamRunner {

    public static final Logger logger = LoggerFactory.getLogger(TwitterV2KafkaStreamRunner.class);
    private final TwitterKafkaIngestionConfigData twitterConfigData;
    private final TwitterV2StreamHelper twitterV2StreamHelper;


    public TwitterV2KafkaStreamRunner(TwitterKafkaIngestionConfigData twitterConfigData, TwitterV2StreamHelper twitterV2StreamHelper) {
        this.twitterConfigData = twitterConfigData;
        this.twitterV2StreamHelper = twitterV2StreamHelper;
    }


    @Override
    public void start()  {
        logger.info("twitterConfigData." + twitterConfigData.getGreetingMessage());
        String bearerToken = twitterConfigData.getTwitterV2BearerToken();
        logger.info("bearerToken: " + bearerToken);
        if(null!= bearerToken){
            try {
                twitterV2StreamHelper.setupRules(bearerToken, getRules());
                twitterV2StreamHelper.connectStream(bearerToken);
            } catch (IOException | URISyntaxException e) {
                logger.error("error streaming tweets", e);
                throw new RuntimeException("error streaming tweets",e);
            }
        }else{
            logger.error("Problem occurred getting bearer token");
            throw new RuntimeException("Problem occurred getting bearer token");
        }
    }

    private Map<String, String> getRules() {

        List <String> keywords = twitterConfigData.getTwitterKeywords();
        Map <String, String> rules= new HashMap<>();
        for(String keyword:keywords){
            rules.put(keyword, "keyword: " + keyword);
        }
        logger.info("Filter created for twittwr keywords: {}", keywords);
        logger.info("Filter created for twittwr keywords: {}", rules);
        return rules;

    }
}
