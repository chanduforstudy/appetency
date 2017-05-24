package com.fa.app.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fa.app.store.service.StoreService;
import com.fa.app.store.web.rest.util.HeaderUtil;
import com.fa.app.store.web.rest.util.PaginationUtil;
import com.fa.app.store.service.dto.StoreDTO;
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
 * REST controller for managing Store.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    private static final String ENTITY_NAME = "store";
        
    private final StoreService storeService;

    public StoreResource(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * POST  /stores : Create a new store.
     *
     * @param storeDTO the storeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeDTO, or with status 400 (Bad Request) if the store has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stores")
    @Timed
    public ResponseEntity<StoreDTO> createStore(@Valid @RequestBody StoreDTO storeDTO) throws URISyntaxException {
        log.debug("REST request to save Store : {}", storeDTO);
        if (storeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new store cannot already have an ID")).body(null);
        }
        StoreDTO result = storeService.save(storeDTO);
        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stores : Updates an existing store.
     *
     * @param storeDTO the storeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeDTO,
     * or with status 400 (Bad Request) if the storeDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stores")
    @Timed
    public ResponseEntity<StoreDTO> updateStore(@Valid @RequestBody StoreDTO storeDTO) throws URISyntaxException {
        log.debug("REST request to update Store : {}", storeDTO);
        if (storeDTO.getId() == null) {
            return createStore(storeDTO);
        }
        StoreDTO result = storeService.save(storeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stores : get all the stores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stores in body
     */
    @GetMapping("/stores")
    @Timed
    public ResponseEntity<List<StoreDTO>> getAllStores(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stores");
        Page<StoreDTO> page = storeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stores/:id : get the "id" store.
     *
     * @param id the id of the storeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stores/{id}")
    @Timed
    public ResponseEntity<StoreDTO> getStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        StoreDTO storeDTO = storeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(storeDTO));
    }

    /**
     * DELETE  /stores/:id : delete the "id" store.
     *
     * @param id the id of the storeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stores?query=:query : search for the store corresponding
     * to the query.
     *
     * @param query the query of the store search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stores")
    @Timed
    public ResponseEntity<List<StoreDTO>> searchStores(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Stores for query {}", query);
        Page<StoreDTO> page = storeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
