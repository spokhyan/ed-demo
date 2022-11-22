package com.techbytes.ed.services;


import com.techbytes.ed.init.StreamInitializer;
import com.techbytes.ed.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.techbytes.ed")
public class TwitterKafkaIngestionApplication implements CommandLineRunner {
    public static final Logger logger = LoggerFactory.getLogger(TwitterKafkaIngestionApplication.class);

    //private final TwitterKafkaIngestionConfigData twitterKafkaIngestionConfigData;
    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;

   // private final TestComponentforConfig testComponentforConfig;




    public TwitterKafkaIngestionApplication(StreamRunner streamRunner, StreamInitializer streamInitializer) {
       // this.twitterKafkaIngestionConfigData = configData;
        this.streamRunner = streamRunner;
       // this.testComponentforConfig = testComponentforConfig;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterKafkaIngestionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Application started ...");

       // testComponentforConfig.printConfigData();
        streamInitializer.init();
        streamRunner.start();
    }
}
