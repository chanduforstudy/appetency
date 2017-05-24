package com.fa.app.store.service;

import com.fa.app.store.service.dto.BusinessUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing BusinessUser.
 */
public interface BusinessUserService {

    /**
     * Save a businessUser.
     *
     * @param businessUserDTO the entity to save
     * @return the persisted entity
     */
    BusinessUserDTO save(BusinessUserDTO businessUserDTO);

    /**
     *  Get all the businessUsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BusinessUserDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" businessUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BusinessUserDTO findOne(Long id);

    /**
     *  Delete the "id" businessUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the businessUser corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BusinessUserDTO> search(String query, Pageable pageable);
}
