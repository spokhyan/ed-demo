package com.techbytes.ed.elastic.index.client.service.impl;

import com.techbytes.ed.config.ElasticConfigData;
import com.techbytes.ed.elastic.index.client.service.ElasticIndexClient;
import com.techbytes.ed.elastic.index.client.util.ElasticUtilIndex;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "false")
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModelImpl> {

    private static final Logger logger = LoggerFactory.getLogger(TwitterElasticIndexClient.class);

    private final ElasticConfigData elasticConfigData;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticUtilIndex<TwitterIndexModelImpl> elasticIndexUtil;

    public TwitterElasticIndexClient(ElasticConfigData configData,
                                     ElasticsearchOperations elasticOperations,
                                     ElasticUtilIndex<TwitterIndexModelImpl> indexUtil) {
        this.elasticConfigData = configData;
        this.elasticsearchOperations = elasticOperations;
        this.elasticIndexUtil = indexUtil;
    }



    @Override
    public List<String> save(List<TwitterIndexModelImpl> documents) {

        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);

        List<String> documentIds = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticConfigData.getIndexName())
        ).stream().map(IndexedObjectInformation::getId).collect(Collectors.toList());

        logger.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModelImpl.class.getName(),
                documentIds);

        return documentIds;
    }
}
