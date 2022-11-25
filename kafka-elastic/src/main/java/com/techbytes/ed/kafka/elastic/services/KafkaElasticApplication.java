package com.techbytes.ed.kafka.elastic.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.techbytes.ed")
public class KafkaElasticApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaElasticApplication.class, args);
    }

}
