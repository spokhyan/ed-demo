package com.techbytes.ed.analytics.service.business.impl;


import com.techbytes.ed.analytics.service.business.AnalyticsService;
import com.techbytes.ed.analytics.service.dataaccess.repo.AnalyticsRepository;
import com.techbytes.ed.analytics.service.model.AnalyticsResponseModel;
import com.techbytes.ed.analytics.service.transformer.EntityToResponseModelTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TwitterAnalyticsService implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    public TwitterAnalyticsService(AnalyticsRepository repository,
                                   EntityToResponseModelTransformer transformer) {
        this.analyticsRepository = repository;
        this.entityToResponseModelTransformer = transformer;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntitiesByWord(word, PageRequest.of(0, 1))
                        .stream().findFirst().orElse(null));
    }
}
