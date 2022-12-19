package com.techbytes.ed.elastic.reactive.query.service.impl;


import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import com.techbytes.ed.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.techbytes.ed.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.techbytes.ed.elastic.reactive.query.service.ElasticQueryService;
import com.techbytes.ed.elastic.reactive.query.service.ReactiveElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ReactiveElasticQueryClient<TwitterIndexModelImpl> reactiveElasticQueryClient;

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    public TwitterElasticQueryService(ReactiveElasticQueryClient<TwitterIndexModelImpl> elasticQueryClient,
                                      ElasticToResponseModelTransformer transformer) {
        this.reactiveElasticQueryClient = elasticQueryClient;
        this.elasticToResponseModelTransformer = transformer;
    }


    @Override
    public Flux<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying reactive elasticsearch for text {}", text);
        return reactiveElasticQueryClient
                .getIndexModelByText(text)
                .map(elasticToResponseModelTransformer::getResponseModel);
    }
}
