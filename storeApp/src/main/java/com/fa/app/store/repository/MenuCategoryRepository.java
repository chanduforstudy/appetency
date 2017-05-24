package com.fa.app.store.repository;

import com.fa.app.store.domain.MenuCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MenuCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Long> {

}
