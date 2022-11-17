package com.techbytes.ed.runner.impl;

import com.techbytes.ed.config.TwitterKafkaIngestionConfigData;
import com.techbytes.ed.listener.TwitterKafkaStatusListener;
import com.techbytes.ed.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Component
@ConditionalOnProperty(name = "twitter-kafka-ingestion.enable-v2-tweets", havingValue = "false")
public class TwitterKafkaStreamRunner implements StreamRunner {
    private static final Logger logger = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);
    private final TwitterKafkaIngestionConfigData twitterConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private TwitterStream twitterStream;

    public TwitterKafkaStreamRunner(TwitterKafkaIngestionConfigData twitterConfigData,
                                   TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterConfigData = twitterConfigData;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);
        addFilter();
    }

    @PreDestroy
    public void shutdown(){
        if(twitterStream != null){
            logger.info("cleaning twitter stream");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = twitterConfigData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        logger.info("display filtered twitter stream for keywords {}", Arrays.toString(keywords));
    }
}
