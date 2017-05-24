package com.fa.app.store.repository.search;

import com.fa.app.store.domain.DiningTable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DiningTable entity.
 */
public interface DiningTableSearchRepository extends ElasticsearchRepository<DiningTable, Long> {
}
