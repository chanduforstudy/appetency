package com.fa.app.store.service.impl;

import com.fa.app.store.service.BusinessUserService;
import com.fa.app.store.domain.BusinessUser;
import com.fa.app.store.repository.BusinessUserRepository;
import com.fa.app.store.repository.search.BusinessUserSearchRepository;
import com.fa.app.store.service.dto.BusinessUserDTO;
import com.fa.app.store.service.mapper.BusinessUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BusinessUser.
 */
@Service
@Transactional
public class BusinessUserServiceImpl implements BusinessUserService{

    private final Logger log = LoggerFactory.getLogger(BusinessUserServiceImpl.class);
    
    private final BusinessUserRepository businessUserRepository;

    private final BusinessUserMapper businessUserMapper;

    private final BusinessUserSearchRepository businessUserSearchRepository;

    public BusinessUserServiceImpl(BusinessUserRepository businessUserRepository, BusinessUserMapper businessUserMapper, BusinessUserSearchRepository businessUserSearchRepository) {
        this.businessUserRepository = businessUserRepository;
        this.businessUserMapper = businessUserMapper;
        this.businessUserSearchRepository = businessUserSearchRepository;
    }

    /**
     * Save a businessUser.
     *
     * @param businessUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BusinessUserDTO save(BusinessUserDTO businessUserDTO) {
        log.debug("Request to save BusinessUser : {}", businessUserDTO);
        BusinessUser businessUser = businessUserMapper.toEntity(businessUserDTO);
        businessUser = businessUserRepository.save(businessUser);
        BusinessUserDTO result = businessUserMapper.toDto(businessUser);
        businessUserSearchRepository.save(businessUser);
        return result;
    }

    /**
     *  Get all the businessUsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BusinessUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessUsers");
        Page<BusinessUser> result = businessUserRepository.findAll(pageable);
        return result.map(businessUser -> businessUserMapper.toDto(businessUser));
    }

    /**
     *  Get one businessUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BusinessUserDTO findOne(Long id) {
        log.debug("Request to get BusinessUser : {}", id);
        BusinessUser businessUser = businessUserRepository.findOne(id);
        BusinessUserDTO businessUserDTO = businessUserMapper.toDto(businessUser);
        return businessUserDTO;
    }

    /**
     *  Delete the  businessUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessUser : {}", id);
        businessUserRepository.delete(id);
        businessUserSearchRepository.delete(id);
    }

    /**
     * Search for the businessUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BusinessUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessUsers for query {}", query);
        Page<BusinessUser> result = businessUserSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(businessUser -> businessUserMapper.toDto(businessUser));
    }
}
