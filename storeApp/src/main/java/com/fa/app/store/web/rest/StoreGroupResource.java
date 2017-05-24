package com.fa.app.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fa.app.store.service.StoreGroupService;
import com.fa.app.store.web.rest.util.HeaderUtil;
import com.fa.app.store.web.rest.util.PaginationUtil;
import com.fa.app.store.service.dto.StoreGroupDTO;
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
 * REST controller for managing StoreGroup.
 */
@RestController
@RequestMapping("/api")
public class StoreGroupResource {

    private final Logger log = LoggerFactory.getLogger(StoreGroupResource.class);

    private static final String ENTITY_NAME = "storeGroup";
        
    private final StoreGroupService storeGroupService;

    public StoreGroupResource(StoreGroupService storeGroupService) {
        this.storeGroupService = storeGroupService;
    }

    /**
     * POST  /store-groups : Create a new storeGroup.
     *
     * @param storeGroupDTO the storeGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeGroupDTO, or with status 400 (Bad Request) if the storeGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store-groups")
    @Timed
    public ResponseEntity<StoreGroupDTO> createStoreGroup(@Valid @RequestBody StoreGroupDTO storeGroupDTO) throws URISyntaxException {
        log.debug("REST request to save StoreGroup : {}", storeGroupDTO);
        if (storeGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new storeGroup cannot already have an ID")).body(null);
        }
        StoreGroupDTO result = storeGroupService.save(storeGroupDTO);
        return ResponseEntity.created(new URI("/api/store-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-groups : Updates an existing storeGroup.
     *
     * @param storeGroupDTO the storeGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeGroupDTO,
     * or with status 400 (Bad Request) if the storeGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeGroupDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/store-groups")
    @Timed
    public ResponseEntity<StoreGroupDTO> updateStoreGroup(@Valid @RequestBody StoreGroupDTO storeGroupDTO) throws URISyntaxException {
        log.debug("REST request to update StoreGroup : {}", storeGroupDTO);
        if (storeGroupDTO.getId() == null) {
            return createStoreGroup(storeGroupDTO);
        }
        StoreGroupDTO result = storeGroupService.save(storeGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-groups : get all the storeGroups.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of storeGroups in body
     */
    @GetMapping("/store-groups")
    @Timed
    public ResponseEntity<List<StoreGroupDTO>> getAllStoreGroups(@ApiParam Pageable pageable, @RequestParam(required = false) String filter) {
        if ("user-is-null".equals(filter)) {
            log.debug("REST request to get all StoreGroups where user is null");
            return new ResponseEntity<>(storeGroupService.findAllWhereUserIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of StoreGroups");
        Page<StoreGroupDTO> page = storeGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/store-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /store-groups/:id : get the "id" storeGroup.
     *
     * @param id the id of the storeGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/store-groups/{id}")
    @Timed
    public ResponseEntity<StoreGroupDTO> getStoreGroup(@PathVariable Long id) {
        log.debug("REST request to get StoreGroup : {}", id);
        StoreGroupDTO storeGroupDTO = storeGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(storeGroupDTO));
    }

    /**
     * DELETE  /store-groups/:id : delete the "id" storeGroup.
     *
     * @param id the id of the storeGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/store-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteStoreGroup(@PathVariable Long id) {
        log.debug("REST request to delete StoreGroup : {}", id);
        storeGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/store-groups?query=:query : search for the storeGroup corresponding
     * to the query.
     *
     * @param query the query of the storeGroup search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/store-groups")
    @Timed
    public ResponseEntity<List<StoreGroupDTO>> searchStoreGroups(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StoreGroups for query {}", query);
        Page<StoreGroupDTO> page = storeGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/store-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
