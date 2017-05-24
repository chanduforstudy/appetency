package com.fa.app.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fa.app.store.service.MenuCategoryService;
import com.fa.app.store.web.rest.util.HeaderUtil;
import com.fa.app.store.web.rest.util.PaginationUtil;
import com.fa.app.store.service.dto.MenuCategoryDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MenuCategory.
 */
@RestController
@RequestMapping("/api")
public class MenuCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MenuCategoryResource.class);

    private static final String ENTITY_NAME = "menuCategory";
        
    private final MenuCategoryService menuCategoryService;

    public MenuCategoryResource(MenuCategoryService menuCategoryService) {
        this.menuCategoryService = menuCategoryService;
    }

    /**
     * POST  /menu-categories : Create a new menuCategory.
     *
     * @param menuCategoryDTO the menuCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new menuCategoryDTO, or with status 400 (Bad Request) if the menuCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/menu-categories")
    @Timed
    public ResponseEntity<MenuCategoryDTO> createMenuCategory(@Valid @RequestBody MenuCategoryDTO menuCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save MenuCategory : {}", menuCategoryDTO);
        if (menuCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new menuCategory cannot already have an ID")).body(null);
        }
        MenuCategoryDTO result = menuCategoryService.save(menuCategoryDTO);
        return ResponseEntity.created(new URI("/api/menu-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menu-categories : Updates an existing menuCategory.
     *
     * @param menuCategoryDTO the menuCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated menuCategoryDTO,
     * or with status 400 (Bad Request) if the menuCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the menuCategoryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/menu-categories")
    @Timed
    public ResponseEntity<MenuCategoryDTO> updateMenuCategory(@Valid @RequestBody MenuCategoryDTO menuCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update MenuCategory : {}", menuCategoryDTO);
        if (menuCategoryDTO.getId() == null) {
            return createMenuCategory(menuCategoryDTO);
        }
        MenuCategoryDTO result = menuCategoryService.save(menuCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, menuCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menu-categories : get all the menuCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of menuCategories in body
     */
    @GetMapping("/menu-categories")
    @Timed
    public ResponseEntity<List<MenuCategoryDTO>> getAllMenuCategories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MenuCategories");
        Page<MenuCategoryDTO> page = menuCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/menu-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /menu-categories/:id : get the "id" menuCategory.
     *
     * @param id the id of the menuCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the menuCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/menu-categories/{id}")
    @Timed
    public ResponseEntity<MenuCategoryDTO> getMenuCategory(@PathVariable Long id) {
        log.debug("REST request to get MenuCategory : {}", id);
        MenuCategoryDTO menuCategoryDTO = menuCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(menuCategoryDTO));
    }

    /**
     * DELETE  /menu-categories/:id : delete the "id" menuCategory.
     *
     * @param id the id of the menuCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/menu-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable Long id) {
        log.debug("REST request to delete MenuCategory : {}", id);
        menuCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/menu-categories?query=:query : search for the menuCategory corresponding
     * to the query.
     *
     * @param query the query of the menuCategory search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/menu-categories")
    @Timed
    public ResponseEntity<List<MenuCategoryDTO>> searchMenuCategories(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MenuCategories for query {}", query);
        Page<MenuCategoryDTO> page = menuCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/menu-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
