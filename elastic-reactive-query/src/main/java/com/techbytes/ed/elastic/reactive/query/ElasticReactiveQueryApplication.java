package com.techbytes.ed.elastic.reactive.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.techbytes.ed")
public class ElasticReactiveQueryApplication {


    public static void main(String[] args) {
        SpringApplication.run(ElasticReactiveQueryApplication.class, args);
    }
}
