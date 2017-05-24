package com.fa.app.store.repository;

import com.fa.app.store.domain.DiningTable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DiningTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable,Long> {

}
