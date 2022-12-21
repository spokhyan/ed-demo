package com.techbytes.ed.analytics.service.business;


import com.techbytes.ed.analytics.service.model.AnalyticsResponseModel;

import java.util.Optional;

public interface AnalyticsService {

    Optional<AnalyticsResponseModel> getWordAnalytics(String word);
}

