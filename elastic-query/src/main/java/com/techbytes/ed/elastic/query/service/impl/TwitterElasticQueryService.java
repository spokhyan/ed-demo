package com.techbytes.ed.elastic.query.service.impl;


import com.techbytes.ed.config.ElasticQueryServiceConfigData;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import com.techbytes.ed.elastic.query.QueryType;
import com.techbytes.ed.elastic.query.client.service.ElasticQueryClient;

import com.techbytes.ed.elastic.query.model.ElasticQueryServiceAnalyticsResponseModel;
import com.techbytes.ed.elastic.query.model.ElasticQueryServiceWordCountResponseModel;
import com.techbytes.ed.elastic.query.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.techbytes.ed.elastic.query.service.ElasticQueryService;
import com.techbytes.ed.elastic.query.service.common.exception.ElasticQueryServiceException;
import com.techbytes.ed.elastic.query.service.common.model.ElasticQueryServiceResponseModel;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;

    private final ElasticQueryClient<TwitterIndexModelImpl> elasticQueryClient;

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;

    private final WebClient.Builder webClientBuilder;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler assembler,
                                      ElasticQueryClient<TwitterIndexModelImpl> queryClient,
                                      ElasticQueryServiceConfigData elasticQueryServiceConfigData,
                                      WebClient.Builder webClientBuilder) {
        this.elasticQueryServiceResponseModelAssembler = assembler;
        this.elasticQueryClient = queryClient;
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}", id);
        return elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentByText(String text, String accessToken) {
        LOG.info("Querying elasticsearch by text {}", text);
        List<ElasticQueryServiceResponseModel> elasticQueryServiceResponseModels =
                elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelByText(text));
        return ElasticQueryServiceAnalyticsResponseModel.builder()
                .queryResponseModels(elasticQueryServiceResponseModels)
                .wordCount(getWordCount(text, accessToken))
                .build();
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elasticsearch");
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }

    private Long getWordCount(String text, String accessToken) {
        if (QueryType.KAFKA_STATE_STORE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromKafkaStateStore(text, accessToken).getWordCount();
        }else if (QueryType.ANALYTICS_DATABASE.getType().
                equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromAnayticsDatabase(text, accessToken).getWordCount();
        }
        return 0L;
    }

    private ElasticQueryServiceWordCountResponseModel getFromAnayticsDatabase(String text, String accessToken) {
        ElasticQueryServiceConfigData.Query queryFromAnalyticsDatabase =
                elasticQueryServiceConfigData.getQueryFromAnalyticsDatabase();
        return retrieveResponseModel(text, accessToken, queryFromAnalyticsDatabase);
    }

    private ElasticQueryServiceWordCountResponseModel getFromKafkaStateStore(String text, String accessToken) {
        ElasticQueryServiceConfigData.Query queryFromKafkaStateStore =
                elasticQueryServiceConfigData.getQueryFromKafkaStateStore();
        return retrieveResponseModel(text, accessToken, queryFromKafkaStateStore);
    }

    private ElasticQueryServiceWordCountResponseModel retrieveResponseModel(String text,
                                                                            String accessToken,
                                                                            ElasticQueryServiceConfigData.Query query) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(query.getMethod()))
                .uri(query.getUri(), uriBuilder -> uriBuilder.build(text))
                .headers(h -> h.setBearerAuth(accessToken))
                .accept(MediaType.valueOf(query.getAccept()))
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(new
                                ElasticQueryServiceException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(ElasticQueryServiceWordCountResponseModel.class)
                .log()
                .block();

    }
}
