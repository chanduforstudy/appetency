package com.fa.app.store.repository;

import com.fa.app.store.domain.TableQR;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TableQR entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TableQRRepository extends JpaRepository<TableQR,Long> {

}
