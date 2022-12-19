package com.techbytes.ed.kafka.stream;


import com.techbytes.ed.kafka.stream.init.StreamsInitializer;
import com.techbytes.ed.kafka.stream.runner.StreamsRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.techbytes.ed"})
public class KafkaStreamsServiceApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsServiceApplication.class);

	private final StreamsRunner<String, Long> streamsRunner;

	private final StreamsInitializer streamsInitializer;

	public KafkaStreamsServiceApplication(StreamsRunner<String, Long> runner,
										  StreamsInitializer initializer) {
		this.streamsRunner = runner;
		this.streamsInitializer = initializer;
	}

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		LOG.info("App starts...");
		streamsInitializer.init();
		streamsRunner.start();
	}

}