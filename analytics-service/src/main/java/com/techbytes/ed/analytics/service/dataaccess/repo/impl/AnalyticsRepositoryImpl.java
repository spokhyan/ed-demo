package com.techbytes.ed.analytics.service.dataaccess.repo.impl;


import com.techbytes.ed.analytics.service.dataaccess.entity.BaseEntity;
import com.techbytes.ed.analytics.service.dataaccess.repo.AnalyticsCustomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public class AnalyticsRepositoryImpl<T extends BaseEntity<PK>, PK> implements AnalyticsCustomRepository<T, PK> {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsRepositoryImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:50}")
    protected int batchSize;


    @Override
    @Transactional
    public <S extends T> PK persist(S entity) {
        this.em.persist(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public <S extends T> void batchPersist(Collection<S> entities) {
        if (entities.isEmpty()) {
            LOG.info("No entity found to insert!");
            return;
        }
        int batchCnt = 0;
        for (S entity: entities) {
            LOG.trace("Persisting entity with id {}", entity.getId());
            this.em.persist(entities);
            batchCnt++;
            if (batchCnt % batchSize == 0) {
                this.em.flush();
                this.em.clear();
            }
        }
        if (batchCnt % batchSize != 0) {
            this.em.flush();
            this.em.clear();
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public <S extends T> S merge(S entity) {
        return this.em.merge(entity);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public <S extends T> void batchMerge(Collection<S> entities) {
        if (entities.isEmpty()) {
            LOG.info("No entity found to insert!");
            return;
        }
        int batchCnt = 0;
        for (S entity : entities) {
            LOG.trace("Merging entity with id {}", entity.getId());
            this.em.merge(entity);
            batchCnt++;
            if (batchCnt % batchSize == 0) {
                this.em.flush();
                this.em.clear();
            }
        }
        if (batchCnt % batchSize != 0) {
            this.em.flush();
            this.em.clear();
        }
    }

    @Override
    public void clear() {

    }
}
