package com.fa.app.store.service;

import com.fa.app.store.service.dto.StoreGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StoreGroup.
 */
public interface StoreGroupService {

    /**
     * Save a storeGroup.
     *
     * @param storeGroupDTO the entity to save
     * @return the persisted entity
     */
    StoreGroupDTO save(StoreGroupDTO storeGroupDTO);

    /**
     *  Get all the storeGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StoreGroupDTO> findAll(Pageable pageable);
    /**
     *  Get all the StoreGroupDTO where User is null.
     *
     *  @return the list of entities
     */
    List<StoreGroupDTO> findAllWhereUserIsNull();

    /**
     *  Get the "id" storeGroup.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StoreGroupDTO findOne(Long id);

    /**
     *  Delete the "id" storeGroup.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the storeGroup corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StoreGroupDTO> search(String query, Pageable pageable);
}
