package com.techbytes.ed.elastic.query.client.service.impl;

import com.techbytes.ed.config.ElasticConfigData;
import com.techbytes.ed.config.ElasticQueryConfigData;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import com.techbytes.ed.elastic.query.client.exception.ElasticQueryClientException;
import com.techbytes.ed.elastic.query.client.service.ElasticQueryClient;
import com.techbytes.ed.elastic.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModelImpl> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;

    private final ElasticQueryConfigData elasticQueryConfigData;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticQueryUtil<TwitterIndexModelImpl> elasticQueryUtil;

    public TwitterElasticQueryClient(ElasticConfigData configData,
                                     ElasticQueryConfigData queryConfigData,
                                     ElasticsearchOperations elasticOperations,
                                     ElasticQueryUtil<TwitterIndexModelImpl> queryUtil) {
        this.elasticConfigData = configData;
        this.elasticQueryConfigData = queryConfigData;
        this.elasticsearchOperations = elasticOperations;
        this.elasticQueryUtil = queryUtil;
    }

    @Override
    public TwitterIndexModelImpl getIndexModelById(String id) {
        Query query = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModelImpl> searchResult = elasticsearchOperations.searchOne(query, TwitterIndexModelImpl.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (searchResult == null) {
            LOG.error("No document found at elasticsearch with id {}", id);
            throw new ElasticQueryClientException("No document found at elasticsearch with id " + id);
        }
        LOG.info("Document with id {} retrieved successfully", searchResult.getId());
        return searchResult.getContent();
    }

    @Override
    public List<TwitterIndexModelImpl> getIndexModelByText(String text) {
        Query query = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        return search(query, "{} of documents with text {} retrieved successfully", text);
    }

    @Override
    public List<TwitterIndexModelImpl> getAllIndexModels() {
        Query query = elasticQueryUtil.getSearchQueryForAll();
        return search(query, "{} number of documents retrieved successfully");
    }

    private List<TwitterIndexModelImpl> search(Query query, String logMessage, Object... logParams) {
        SearchHits<TwitterIndexModelImpl> searchResult = elasticsearchOperations.search(query, TwitterIndexModelImpl.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info(logMessage, searchResult.getTotalHits(), logParams);
        return searchResult.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
