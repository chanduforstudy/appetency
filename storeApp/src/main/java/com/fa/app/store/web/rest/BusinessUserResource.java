package com.fa.app.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fa.app.store.service.BusinessUserService;
import com.fa.app.store.web.rest.util.HeaderUtil;
import com.fa.app.store.web.rest.util.PaginationUtil;
import com.fa.app.store.service.dto.BusinessUserDTO;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BusinessUser.
 */
@RestController
@RequestMapping("/api")
public class BusinessUserResource {

    private final Logger log = LoggerFactory.getLogger(BusinessUserResource.class);

    private static final String ENTITY_NAME = "businessUser";
        
    private final BusinessUserService businessUserService;

    public BusinessUserResource(BusinessUserService businessUserService) {
        this.businessUserService = businessUserService;
    }

    /**
     * POST  /business-users : Create a new businessUser.
     *
     * @param businessUserDTO the businessUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessUserDTO, or with status 400 (Bad Request) if the businessUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/business-users")
    @Timed
    public ResponseEntity<BusinessUserDTO> createBusinessUser(@RequestBody BusinessUserDTO businessUserDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessUser : {}", businessUserDTO);
        if (businessUserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new businessUser cannot already have an ID")).body(null);
        }
        BusinessUserDTO result = businessUserService.save(businessUserDTO);
        return ResponseEntity.created(new URI("/api/business-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-users : Updates an existing businessUser.
     *
     * @param businessUserDTO the businessUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessUserDTO,
     * or with status 400 (Bad Request) if the businessUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the businessUserDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/business-users")
    @Timed
    public ResponseEntity<BusinessUserDTO> updateBusinessUser(@RequestBody BusinessUserDTO businessUserDTO) throws URISyntaxException {
        log.debug("REST request to update BusinessUser : {}", businessUserDTO);
        if (businessUserDTO.getId() == null) {
            return createBusinessUser(businessUserDTO);
        }
        BusinessUserDTO result = businessUserService.save(businessUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-users : get all the businessUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of businessUsers in body
     */
    @GetMapping("/business-users")
    @Timed
    public ResponseEntity<List<BusinessUserDTO>> getAllBusinessUsers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BusinessUsers");
        Page<BusinessUserDTO> page = businessUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/business-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /business-users/:id : get the "id" businessUser.
     *
     * @param id the id of the businessUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/business-users/{id}")
    @Timed
    public ResponseEntity<BusinessUserDTO> getBusinessUser(@PathVariable Long id) {
        log.debug("REST request to get BusinessUser : {}", id);
        BusinessUserDTO businessUserDTO = businessUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessUserDTO));
    }

    /**
     * DELETE  /business-users/:id : delete the "id" businessUser.
     *
     * @param id the id of the businessUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/business-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteBusinessUser(@PathVariable Long id) {
        log.debug("REST request to delete BusinessUser : {}", id);
        businessUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/business-users?query=:query : search for the businessUser corresponding
     * to the query.
     *
     * @param query the query of the businessUser search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/business-users")
    @Timed
    public ResponseEntity<List<BusinessUserDTO>> searchBusinessUsers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of BusinessUsers for query {}", query);
        Page<BusinessUserDTO> page = businessUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/business-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
