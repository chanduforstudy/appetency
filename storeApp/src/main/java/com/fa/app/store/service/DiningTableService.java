package com.fa.app.store.service;

import com.fa.app.store.service.dto.DiningTableDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DiningTable.
 */
public interface DiningTableService {

    /**
     * Save a diningTable.
     *
     * @param diningTableDTO the entity to save
     * @return the persisted entity
     */
    DiningTableDTO save(DiningTableDTO diningTableDTO);

    /**
     *  Get all the diningTables.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DiningTableDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" diningTable.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DiningTableDTO findOne(Long id);

    /**
     *  Delete the "id" diningTable.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the diningTable corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DiningTableDTO> search(String query, Pageable pageable);
}
