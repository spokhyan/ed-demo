package com.techbytes.ed.elastic.reactive.query.repo;


import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<TwitterIndexModelImpl, String> {

    Flux<TwitterIndexModelImpl> findByText(String text);
}
