package com.fa.app.store.service;

import com.fa.app.store.service.dto.StoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Store.
 */
public interface StoreService {

    /**
     * Save a store.
     *
     * @param storeDTO the entity to save
     * @return the persisted entity
     */
    StoreDTO save(StoreDTO storeDTO);

    /**
     *  Get all the stores.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StoreDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" store.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StoreDTO findOne(Long id);

    /**
     *  Delete the "id" store.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the store corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StoreDTO> search(String query, Pageable pageable);
}
