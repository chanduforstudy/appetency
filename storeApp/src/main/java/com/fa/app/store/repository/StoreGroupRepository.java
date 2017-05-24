package com.fa.app.store.repository;

import com.fa.app.store.domain.StoreGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StoreGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreGroupRepository extends JpaRepository<StoreGroup,Long> {

}
