package com.techbytes.ed.elastic.query.service;



import com.techbytes.ed.elastic.query.model.ElasticQueryServiceAnalyticsResponseModel;
import com.techbytes.ed.elastic.query.service.common.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(String id);

    ElasticQueryServiceAnalyticsResponseModel getDocumentByText(String text, String accessToken);

    List<ElasticQueryServiceResponseModel> getAllDocuments();
}
