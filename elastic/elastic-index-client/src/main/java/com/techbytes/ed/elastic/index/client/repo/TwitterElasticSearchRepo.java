package com.techbytes.ed.elastic.index.client.repo;

import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticSearchRepo extends ElasticsearchRepository<TwitterIndexModelImpl, String> {
}
