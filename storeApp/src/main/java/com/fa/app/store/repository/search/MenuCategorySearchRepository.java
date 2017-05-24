package com.fa.app.store.repository.search;

import com.fa.app.store.domain.MenuCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MenuCategory entity.
 */
public interface MenuCategorySearchRepository extends ElasticsearchRepository<MenuCategory, Long> {
}
