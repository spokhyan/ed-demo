package com.techbytes.ed.elastic.query.client.repo;

import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwitterElasticsearchQueryRepository extends ElasticsearchRepository<TwitterIndexModelImpl, String> {

    List<TwitterIndexModelImpl> findByText(String text);
}
