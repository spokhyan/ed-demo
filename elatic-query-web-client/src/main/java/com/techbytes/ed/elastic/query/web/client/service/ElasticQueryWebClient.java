package com.techbytes.ed.elastic.query.web.client.service;

import com.techbytes.ed.elastic.query.web.client.common.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.techbytes.ed.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.techbytes.ed.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;


import java.util.List;

public interface ElasticQueryWebClient {

    ElasticQueryWebClientAnalyticsResponseModel getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
