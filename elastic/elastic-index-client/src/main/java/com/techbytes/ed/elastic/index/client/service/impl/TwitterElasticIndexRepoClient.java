package com.techbytes.ed.elastic.index.client.service.impl;


import com.techbytes.ed.elastic.index.client.repo.TwitterElasticSearchRepo;
import com.techbytes.ed.elastic.index.client.service.ElasticIndexClient;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
//@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticIndexRepoClient implements ElasticIndexClient<TwitterIndexModelImpl> {
    private static final Logger logger = LoggerFactory.getLogger(TwitterElasticIndexRepoClient.class);

    private final TwitterElasticSearchRepo twitterElasticSearchRepo;

    public TwitterElasticIndexRepoClient(TwitterElasticSearchRepo twitterElasticSearchRepo) {
        this.twitterElasticSearchRepo = twitterElasticSearchRepo;
    }


    @Override
    public List<String> save(List<TwitterIndexModelImpl> documents) {
        List<TwitterIndexModelImpl> repoResponse= (List<TwitterIndexModelImpl>) twitterElasticSearchRepo.saveAll(documents);
        List<String> ids = repoResponse.stream().map(TwitterIndexModelImpl::getId).collect(Collectors.toList());
        logger.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModelImpl.class.getName(),
                ids);
        return ids;
    }
}
