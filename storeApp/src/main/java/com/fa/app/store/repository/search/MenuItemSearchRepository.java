package com.fa.app.store.repository.search;

import com.fa.app.store.domain.MenuItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MenuItem entity.
 */
public interface MenuItemSearchRepository extends ElasticsearchRepository<MenuItem, Long> {
}
