package com.techbytes.ed.elastic.reactive.query.service;


import com.techbytes.ed.elastic.model.ElasticIndexModel;
import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;
import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends ElasticIndexModel> {

    Flux<TwitterIndexModelImpl> getIndexModelByText(String text);
}
