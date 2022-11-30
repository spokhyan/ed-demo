package com.techbytes.ed.elastic.query.client.service;

import com.techbytes.ed.elastic.model.ElasticIndexModel;

import java.util.List;

public interface ElasticQueryClient<T extends ElasticIndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelByText(String text);

    List<T> getAllIndexModels();
}
