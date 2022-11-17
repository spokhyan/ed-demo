package com.techbytes.ed.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

    @Override
    public void onStatus(Status status) {
        logger.info("Twitter Status {}", status.getText());
    }
}
