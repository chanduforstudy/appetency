package com.fa.app.store.service.impl;

import com.fa.app.store.service.StoreService;
import com.fa.app.store.domain.Store;
import com.fa.app.store.repository.StoreRepository;
import com.fa.app.store.repository.search.StoreSearchRepository;
import com.fa.app.store.service.dto.StoreDTO;
import com.fa.app.store.service.mapper.StoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Store.
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService{

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);
    
    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    private final StoreSearchRepository storeSearchRepository;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper, StoreSearchRepository storeSearchRepository) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.storeSearchRepository = storeSearchRepository;
    }

    /**
     * Save a store.
     *
     * @param storeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        log.debug("Request to save Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        StoreDTO result = storeMapper.toDto(store);
        storeSearchRepository.save(store);
        return result;
    }

    /**
     *  Get all the stores.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stores");
        Page<Store> result = storeRepository.findAll(pageable);
        return result.map(store -> storeMapper.toDto(store));
    }

    /**
     *  Get one store by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StoreDTO findOne(Long id) {
        log.debug("Request to get Store : {}", id);
        Store store = storeRepository.findOne(id);
        StoreDTO storeDTO = storeMapper.toDto(store);
        return storeDTO;
    }

    /**
     *  Delete the  store by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.delete(id);
        storeSearchRepository.delete(id);
    }

    /**
     * Search for the store corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stores for query {}", query);
        Page<Store> result = storeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(store -> storeMapper.toDto(store));
    }
}
