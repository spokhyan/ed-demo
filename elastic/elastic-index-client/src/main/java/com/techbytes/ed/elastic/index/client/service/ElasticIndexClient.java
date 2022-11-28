package com.techbytes.ed.elastic.index.client.service;

import com.techbytes.ed.elastic.model.ElasticIndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends ElasticIndexModel> {
    List<String> save(List<T> documents);
}
