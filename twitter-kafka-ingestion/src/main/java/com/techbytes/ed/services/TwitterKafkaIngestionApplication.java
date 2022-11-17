package com.techbytes.ed.services;


import com.techbytes.ed.config.TwitterKafkaIngestionConfigData;
import com.techbytes.ed.listener.TestComponentforConfig;
import com.techbytes.ed.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.techbytes.ed")
public class TwitterKafkaIngestionApplication implements CommandLineRunner {
    public static final Logger logger = LoggerFactory.getLogger(TwitterKafkaIngestionApplication.class);

    private final TwitterKafkaIngestionConfigData twitterKafkaIngestionConfigData;
    private final StreamRunner streamRunner;

    private final TestComponentforConfig testComponentforConfig;




    public TwitterKafkaIngestionApplication(TwitterKafkaIngestionConfigData configData, StreamRunner streamRunner, TestComponentforConfig testComponentforConfig) {
        this.twitterKafkaIngestionConfigData = configData;
        this.streamRunner = streamRunner;
        this.testComponentforConfig = testComponentforConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterKafkaIngestionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Application started ...");
        logger.info(Arrays.toString(twitterKafkaIngestionConfigData.getTwitterKeywords().toArray(new String[] {})));
        logger.info(twitterKafkaIngestionConfigData.getGreetingMessage());
        logger.info("twitterConfigData" + twitterKafkaIngestionConfigData.getGreetingMessage());
        testComponentforConfig.printConfigData();
        streamRunner.start();
    }
}
