package com.fa.app.store.repository.search;

import com.fa.app.store.domain.BusinessUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusinessUser entity.
 */
public interface BusinessUserSearchRepository extends ElasticsearchRepository<BusinessUser, Long> {
}
