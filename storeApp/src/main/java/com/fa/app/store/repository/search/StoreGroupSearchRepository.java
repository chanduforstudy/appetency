package com.fa.app.store.repository.search;

import com.fa.app.store.domain.StoreGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StoreGroup entity.
 */
public interface StoreGroupSearchRepository extends ElasticsearchRepository<StoreGroup, Long> {
}
