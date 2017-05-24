package com.fa.app.store.repository;

import com.fa.app.store.domain.BusinessUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BusinessUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessUserRepository extends JpaRepository<BusinessUser,Long> {

}
