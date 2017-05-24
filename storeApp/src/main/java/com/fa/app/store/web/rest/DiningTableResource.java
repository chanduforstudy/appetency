package com.fa.app.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fa.app.store.service.DiningTableService;
import com.fa.app.store.web.rest.util.HeaderUtil;
import com.fa.app.store.web.rest.util.PaginationUtil;
import com.fa.app.store.service.dto.DiningTableDTO;
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
 * REST controller for managing DiningTable.
 */
@RestController
@RequestMapping("/api")
public class DiningTableResource {

    private final Logger log = LoggerFactory.getLogger(DiningTableResource.class);

    private static final String ENTITY_NAME = "diningTable";
        
    private final DiningTableService diningTableService;

    public DiningTableResource(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }

    /**
     * POST  /dining-tables : Create a new diningTable.
     *
     * @param diningTableDTO the diningTableDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diningTableDTO, or with status 400 (Bad Request) if the diningTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dining-tables")
    @Timed
    public ResponseEntity<DiningTableDTO> createDiningTable(@Valid @RequestBody DiningTableDTO diningTableDTO) throws URISyntaxException {
        log.debug("REST request to save DiningTable : {}", diningTableDTO);
        if (diningTableDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new diningTable cannot already have an ID")).body(null);
        }
        DiningTableDTO result = diningTableService.save(diningTableDTO);
        return ResponseEntity.created(new URI("/api/dining-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dining-tables : Updates an existing diningTable.
     *
     * @param diningTableDTO the diningTableDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diningTableDTO,
     * or with status 400 (Bad Request) if the diningTableDTO is not valid,
     * or with status 500 (Internal Server Error) if the diningTableDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dining-tables")
    @Timed
    public ResponseEntity<DiningTableDTO> updateDiningTable(@Valid @RequestBody DiningTableDTO diningTableDTO) throws URISyntaxException {
        log.debug("REST request to update DiningTable : {}", diningTableDTO);
        if (diningTableDTO.getId() == null) {
            return createDiningTable(diningTableDTO);
        }
        DiningTableDTO result = diningTableService.save(diningTableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, diningTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dining-tables : get all the diningTables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of diningTables in body
     */
    @GetMapping("/dining-tables")
    @Timed
    public ResponseEntity<List<DiningTableDTO>> getAllDiningTables(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DiningTables");
        Page<DiningTableDTO> page = diningTableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dining-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dining-tables/:id : get the "id" diningTable.
     *
     * @param id the id of the diningTableDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diningTableDTO, or with status 404 (Not Found)
     */
    @GetMapping("/dining-tables/{id}")
    @Timed
    public ResponseEntity<DiningTableDTO> getDiningTable(@PathVariable Long id) {
        log.debug("REST request to get DiningTable : {}", id);
        DiningTableDTO diningTableDTO = diningTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(diningTableDTO));
    }

    /**
     * DELETE  /dining-tables/:id : delete the "id" diningTable.
     *
     * @param id the id of the diningTableDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dining-tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiningTable(@PathVariable Long id) {
        log.debug("REST request to delete DiningTable : {}", id);
        diningTableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dining-tables?query=:query : search for the diningTable corresponding
     * to the query.
     *
     * @param query the query of the diningTable search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/dining-tables")
    @Timed
    public ResponseEntity<List<DiningTableDTO>> searchDiningTables(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DiningTables for query {}", query);
        Page<DiningTableDTO> page = diningTableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/dining-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
