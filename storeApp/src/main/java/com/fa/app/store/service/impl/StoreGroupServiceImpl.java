package com.fa.app.store.service.impl;

import com.fa.app.store.service.StoreGroupService;
import com.fa.app.store.domain.StoreGroup;
import com.fa.app.store.repository.StoreGroupRepository;
import com.fa.app.store.repository.search.StoreGroupSearchRepository;
import com.fa.app.store.service.dto.StoreGroupDTO;
import com.fa.app.store.service.mapper.StoreGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StoreGroup.
 */
@Service
@Transactional
public class StoreGroupServiceImpl implements StoreGroupService{

    private final Logger log = LoggerFactory.getLogger(StoreGroupServiceImpl.class);
    
    private final StoreGroupRepository storeGroupRepository;

    private final StoreGroupMapper storeGroupMapper;

    private final StoreGroupSearchRepository storeGroupSearchRepository;

    public StoreGroupServiceImpl(StoreGroupRepository storeGroupRepository, StoreGroupMapper storeGroupMapper, StoreGroupSearchRepository storeGroupSearchRepository) {
        this.storeGroupRepository = storeGroupRepository;
        this.storeGroupMapper = storeGroupMapper;
        this.storeGroupSearchRepository = storeGroupSearchRepository;
    }

    /**
     * Save a storeGroup.
     *
     * @param storeGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StoreGroupDTO save(StoreGroupDTO storeGroupDTO) {
        log.debug("Request to save StoreGroup : {}", storeGroupDTO);
        StoreGroup storeGroup = storeGroupMapper.toEntity(storeGroupDTO);
        storeGroup = storeGroupRepository.save(storeGroup);
        StoreGroupDTO result = storeGroupMapper.toDto(storeGroup);
        storeGroupSearchRepository.save(storeGroup);
        return result;
    }

    /**
     *  Get all the storeGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StoreGroups");
        Page<StoreGroup> result = storeGroupRepository.findAll(pageable);
        return result.map(storeGroup -> storeGroupMapper.toDto(storeGroup));
    }


    /**
     *  get all the storeGroups where User is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<StoreGroupDTO> findAllWhereUserIsNull() {
        log.debug("Request to get all storeGroups where User is null");
        return StreamSupport
            .stream(storeGroupRepository.findAll().spliterator(), false)
            .filter(storeGroup -> storeGroup.getUser() == null)
            .map(storeGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one storeGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StoreGroupDTO findOne(Long id) {
        log.debug("Request to get StoreGroup : {}", id);
        StoreGroup storeGroup = storeGroupRepository.findOne(id);
        StoreGroupDTO storeGroupDTO = storeGroupMapper.toDto(storeGroup);
        return storeGroupDTO;
    }

    /**
     *  Delete the  storeGroup by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StoreGroup : {}", id);
        storeGroupRepository.delete(id);
        storeGroupSearchRepository.delete(id);
    }

    /**
     * Search for the storeGroup corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StoreGroups for query {}", query);
        Page<StoreGroup> result = storeGroupSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(storeGroup -> storeGroupMapper.toDto(storeGroup));
    }
}
