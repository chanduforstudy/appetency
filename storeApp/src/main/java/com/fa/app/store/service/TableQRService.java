package com.fa.app.store.service;

import com.fa.app.store.service.dto.TableQRDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TableQR.
 */
public interface TableQRService {

    /**
     * Save a tableQR.
     *
     * @param tableQRDTO the entity to save
     * @return the persisted entity
     */
    TableQRDTO save(TableQRDTO tableQRDTO);

    /**
     *  Get all the tableQRS.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TableQRDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tableQR.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TableQRDTO findOne(Long id);

    /**
     *  Delete the "id" tableQR.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the tableQR corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TableQRDTO> search(String query, Pageable pageable);
}
