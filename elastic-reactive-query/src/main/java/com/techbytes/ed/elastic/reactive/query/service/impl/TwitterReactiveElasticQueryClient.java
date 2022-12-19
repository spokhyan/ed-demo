package com.techbytes.ed.elastic.reactive.query.service.impl;


import com.techbytes.ed.config.ElasticQueryServiceConfigData;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import com.techbytes.ed.elastic.reactive.query.repo.ElasticQueryRepository;
import com.techbytes.ed.elastic.reactive.query.service.ReactiveElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class TwitterReactiveElasticQueryClient implements ReactiveElasticQueryClient<TwitterIndexModelImpl> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterReactiveElasticQueryClient.class);

    private final ElasticQueryRepository elasticQueryRepository;

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;

    public TwitterReactiveElasticQueryClient(ElasticQueryRepository elasticRepository,
                                             ElasticQueryServiceConfigData configData) {
        this.elasticQueryRepository = elasticRepository;
        this.elasticQueryServiceConfigData = configData;
    }


    @Override
    public Flux<TwitterIndexModelImpl> getIndexModelByText(String text) {
        LOG.info("Getting data from elasticsearch for text {}", text);
        return elasticQueryRepository
                .findByText(text)
                .delayElements(Duration.ofMillis(elasticQueryServiceConfigData.getBackPressureDelayMs()));
    }
}
