package com.fa.app.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fa.app.store.service.TableQRService;
import com.fa.app.store.web.rest.util.HeaderUtil;
import com.fa.app.store.web.rest.util.PaginationUtil;
import com.fa.app.store.service.dto.TableQRDTO;
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
 * REST controller for managing TableQR.
 */
@RestController
@RequestMapping("/api")
public class TableQRResource {

    private final Logger log = LoggerFactory.getLogger(TableQRResource.class);

    private static final String ENTITY_NAME = "tableQR";
        
    private final TableQRService tableQRService;

    public TableQRResource(TableQRService tableQRService) {
        this.tableQRService = tableQRService;
    }

    /**
     * POST  /table-qrs : Create a new tableQR.
     *
     * @param tableQRDTO the tableQRDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tableQRDTO, or with status 400 (Bad Request) if the tableQR has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/table-qrs")
    @Timed
    public ResponseEntity<TableQRDTO> createTableQR(@Valid @RequestBody TableQRDTO tableQRDTO) throws URISyntaxException {
        log.debug("REST request to save TableQR : {}", tableQRDTO);
        if (tableQRDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tableQR cannot already have an ID")).body(null);
        }
        TableQRDTO result = tableQRService.save(tableQRDTO);
        return ResponseEntity.created(new URI("/api/table-qrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /table-qrs : Updates an existing tableQR.
     *
     * @param tableQRDTO the tableQRDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tableQRDTO,
     * or with status 400 (Bad Request) if the tableQRDTO is not valid,
     * or with status 500 (Internal Server Error) if the tableQRDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/table-qrs")
    @Timed
    public ResponseEntity<TableQRDTO> updateTableQR(@Valid @RequestBody TableQRDTO tableQRDTO) throws URISyntaxException {
        log.debug("REST request to update TableQR : {}", tableQRDTO);
        if (tableQRDTO.getId() == null) {
            return createTableQR(tableQRDTO);
        }
        TableQRDTO result = tableQRService.save(tableQRDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tableQRDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /table-qrs : get all the tableQRS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tableQRS in body
     */
    @GetMapping("/table-qrs")
    @Timed
    public ResponseEntity<List<TableQRDTO>> getAllTableQRS(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TableQRS");
        Page<TableQRDTO> page = tableQRService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/table-qrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /table-qrs/:id : get the "id" tableQR.
     *
     * @param id the id of the tableQRDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tableQRDTO, or with status 404 (Not Found)
     */
    @GetMapping("/table-qrs/{id}")
    @Timed
    public ResponseEntity<TableQRDTO> getTableQR(@PathVariable Long id) {
        log.debug("REST request to get TableQR : {}", id);
        TableQRDTO tableQRDTO = tableQRService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tableQRDTO));
    }

    /**
     * DELETE  /table-qrs/:id : delete the "id" tableQR.
     *
     * @param id the id of the tableQRDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/table-qrs/{id}")
    @Timed
    public ResponseEntity<Void> deleteTableQR(@PathVariable Long id) {
        log.debug("REST request to delete TableQR : {}", id);
        tableQRService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/table-qrs?query=:query : search for the tableQR corresponding
     * to the query.
     *
     * @param query the query of the tableQR search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/table-qrs")
    @Timed
    public ResponseEntity<List<TableQRDTO>> searchTableQRS(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TableQRS for query {}", query);
        Page<TableQRDTO> page = tableQRService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/table-qrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
