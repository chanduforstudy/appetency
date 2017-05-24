package com.fa.app.store.repository;

import com.fa.app.store.domain.MenuItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {

}
