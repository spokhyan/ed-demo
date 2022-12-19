package com.techbytes.ed.elastic.query.service.common.transformer;


import com.techbytes.ed.elastic.model.impl.TwitterIndexModelImpl;

import com.techbytes.ed.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {



    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModelImpl twitterIndexModel) {
        return ElasticQueryServiceResponseModel
                .builder()
                .id(twitterIndexModel.getId())
                .userId(twitterIndexModel.getUserId())
                .text(twitterIndexModel.getText())
                .createdAt(twitterIndexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel>
        getResponseModels(List<TwitterIndexModelImpl> twitterIndexModels) {
        return twitterIndexModels.stream().
                map(this::getResponseModel).collect(Collectors.toList());
    }
}
