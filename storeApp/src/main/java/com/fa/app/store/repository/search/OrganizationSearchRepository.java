package com.fa.app.store.repository.search;

import com.fa.app.store.domain.Organization;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Organization entity.
 */
public interface OrganizationSearchRepository extends ElasticsearchRepository<Organization, Long> {
}
