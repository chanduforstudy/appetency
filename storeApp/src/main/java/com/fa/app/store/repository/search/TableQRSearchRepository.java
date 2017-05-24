package com.fa.app.store.repository.search;

import com.fa.app.store.domain.TableQR;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TableQR entity.
 */
public interface TableQRSearchRepository extends ElasticsearchRepository<TableQR, Long> {
}
