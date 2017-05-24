package com.fa.app.store.service.impl;

import com.fa.app.store.service.MenuItemService;
import com.fa.app.store.domain.MenuItem;
import com.fa.app.store.repository.MenuItemRepository;
import com.fa.app.store.repository.search.MenuItemSearchRepository;
import com.fa.app.store.service.dto.MenuItemDTO;
import com.fa.app.store.service.mapper.MenuItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MenuItem.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService{

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);
    
    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    private final MenuItemSearchRepository menuItemSearchRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper, MenuItemSearchRepository menuItemSearchRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
        this.menuItemSearchRepository = menuItemSearchRepository;
    }

    /**
     * Save a menuItem.
     *
     * @param menuItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MenuItemDTO save(MenuItemDTO menuItemDTO) {
        log.debug("Request to save MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        MenuItemDTO result = menuItemMapper.toDto(menuItem);
        menuItemSearchRepository.save(menuItem);
        return result;
    }

    /**
     *  Get all the menuItems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        Page<MenuItem> result = menuItemRepository.findAll(pageable);
        return result.map(menuItem -> menuItemMapper.toDto(menuItem));
    }

    /**
     *  Get one menuItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MenuItemDTO findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        MenuItem menuItem = menuItemRepository.findOne(id);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);
        return menuItemDTO;
    }

    /**
     *  Delete the  menuItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.delete(id);
        menuItemSearchRepository.delete(id);
    }

    /**
     * Search for the menuItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MenuItems for query {}", query);
        Page<MenuItem> result = menuItemSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(menuItem -> menuItemMapper.toDto(menuItem));
    }
}
