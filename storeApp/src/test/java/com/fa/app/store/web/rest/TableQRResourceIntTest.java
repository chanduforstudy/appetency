package com.fa.app.store.web.rest;

import com.fa.app.store.StoreApp;

import com.fa.app.store.config.SecurityBeanOverrideConfiguration;

import com.fa.app.store.domain.TableQR;
import com.fa.app.store.repository.TableQRRepository;
import com.fa.app.store.service.TableQRService;
import com.fa.app.store.repository.search.TableQRSearchRepository;
import com.fa.app.store.service.dto.TableQRDTO;
import com.fa.app.store.service.mapper.TableQRMapper;
import com.fa.app.store.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TableQRResource REST controller.
 *
 * @see TableQRResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreApp.class, SecurityBeanOverrideConfiguration.class})
public class TableQRResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private TableQRRepository tableQRRepository;

    @Autowired
    private TableQRMapper tableQRMapper;

    @Autowired
    private TableQRService tableQRService;

    @Autowired
    private TableQRSearchRepository tableQRSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTableQRMockMvc;

    private TableQR tableQR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TableQRResource tableQRResource = new TableQRResource(tableQRService);
        this.restTableQRMockMvc = MockMvcBuilders.standaloneSetup(tableQRResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TableQR createEntity(EntityManager em) {
        TableQR tableQR = new TableQR()
            .code(DEFAULT_CODE);
        return tableQR;
    }

    @Before
    public void initTest() {
        tableQRSearchRepository.deleteAll();
        tableQR = createEntity(em);
    }

    @Test
    @Transactional
    public void createTableQR() throws Exception {
        int databaseSizeBeforeCreate = tableQRRepository.findAll().size();

        // Create the TableQR
        TableQRDTO tableQRDTO = tableQRMapper.toDto(tableQR);
        restTableQRMockMvc.perform(post("/api/table-qrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tableQRDTO)))
            .andExpect(status().isCreated());

        // Validate the TableQR in the database
        List<TableQR> tableQRList = tableQRRepository.findAll();
        assertThat(tableQRList).hasSize(databaseSizeBeforeCreate + 1);
        TableQR testTableQR = tableQRList.get(tableQRList.size() - 1);
        assertThat(testTableQR.getCode()).isEqualTo(DEFAULT_CODE);

        // Validate the TableQR in Elasticsearch
        TableQR tableQREs = tableQRSearchRepository.findOne(testTableQR.getId());
        assertThat(tableQREs).isEqualToComparingFieldByField(testTableQR);
    }

    @Test
    @Transactional
    public void createTableQRWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tableQRRepository.findAll().size();

        // Create the TableQR with an existing ID
        tableQR.setId(1L);
        TableQRDTO tableQRDTO = tableQRMapper.toDto(tableQR);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTableQRMockMvc.perform(post("/api/table-qrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tableQRDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TableQR> tableQRList = tableQRRepository.findAll();
        assertThat(tableQRList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tableQRRepository.findAll().size();
        // set the field null
        tableQR.setCode(null);

        // Create the TableQR, which fails.
        TableQRDTO tableQRDTO = tableQRMapper.toDto(tableQR);

        restTableQRMockMvc.perform(post("/api/table-qrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tableQRDTO)))
            .andExpect(status().isBadRequest());

        List<TableQR> tableQRList = tableQRRepository.findAll();
        assertThat(tableQRList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTableQRS() throws Exception {
        // Initialize the database
        tableQRRepository.saveAndFlush(tableQR);

        // Get all the tableQRList
        restTableQRMockMvc.perform(get("/api/table-qrs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tableQR.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getTableQR() throws Exception {
        // Initialize the database
        tableQRRepository.saveAndFlush(tableQR);

        // Get the tableQR
        restTableQRMockMvc.perform(get("/api/table-qrs/{id}", tableQR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tableQR.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTableQR() throws Exception {
        // Get the tableQR
        restTableQRMockMvc.perform(get("/api/table-qrs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTableQR() throws Exception {
        // Initialize the database
        tableQRRepository.saveAndFlush(tableQR);
        tableQRSearchRepository.save(tableQR);
        int databaseSizeBeforeUpdate = tableQRRepository.findAll().size();

        // Update the tableQR
        TableQR updatedTableQR = tableQRRepository.findOne(tableQR.getId());
        updatedTableQR
            .code(UPDATED_CODE);
        TableQRDTO tableQRDTO = tableQRMapper.toDto(updatedTableQR);

        restTableQRMockMvc.perform(put("/api/table-qrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tableQRDTO)))
            .andExpect(status().isOk());

        // Validate the TableQR in the database
        List<TableQR> tableQRList = tableQRRepository.findAll();
        assertThat(tableQRList).hasSize(databaseSizeBeforeUpdate);
        TableQR testTableQR = tableQRList.get(tableQRList.size() - 1);
        assertThat(testTableQR.getCode()).isEqualTo(UPDATED_CODE);

        // Validate the TableQR in Elasticsearch
        TableQR tableQREs = tableQRSearchRepository.findOne(testTableQR.getId());
        assertThat(tableQREs).isEqualToComparingFieldByField(testTableQR);
    }

    @Test
    @Transactional
    public void updateNonExistingTableQR() throws Exception {
        int databaseSizeBeforeUpdate = tableQRRepository.findAll().size();

        // Create the TableQR
        TableQRDTO tableQRDTO = tableQRMapper.toDto(tableQR);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTableQRMockMvc.perform(put("/api/table-qrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tableQRDTO)))
            .andExpect(status().isCreated());

        // Validate the TableQR in the database
        List<TableQR> tableQRList = tableQRRepository.findAll();
        assertThat(tableQRList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTableQR() throws Exception {
        // Initialize the database
        tableQRRepository.saveAndFlush(tableQR);
        tableQRSearchRepository.save(tableQR);
        int databaseSizeBeforeDelete = tableQRRepository.findAll().size();

        // Get the tableQR
        restTableQRMockMvc.perform(delete("/api/table-qrs/{id}", tableQR.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tableQRExistsInEs = tableQRSearchRepository.exists(tableQR.getId());
        assertThat(tableQRExistsInEs).isFalse();

        // Validate the database is empty
        List<TableQR> tableQRList = tableQRRepository.findAll();
        assertThat(tableQRList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTableQR() throws Exception {
        // Initialize the database
        tableQRRepository.saveAndFlush(tableQR);
        tableQRSearchRepository.save(tableQR);

        // Search the tableQR
        restTableQRMockMvc.perform(get("/api/_search/table-qrs?query=id:" + tableQR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tableQR.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TableQR.class);
        TableQR tableQR1 = new TableQR();
        tableQR1.setId(1L);
        TableQR tableQR2 = new TableQR();
        tableQR2.setId(tableQR1.getId());
        assertThat(tableQR1).isEqualTo(tableQR2);
        tableQR2.setId(2L);
        assertThat(tableQR1).isNotEqualTo(tableQR2);
        tableQR1.setId(null);
        assertThat(tableQR1).isNotEqualTo(tableQR2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TableQRDTO.class);
        TableQRDTO tableQRDTO1 = new TableQRDTO();
        tableQRDTO1.setId(1L);
        TableQRDTO tableQRDTO2 = new TableQRDTO();
        assertThat(tableQRDTO1).isNotEqualTo(tableQRDTO2);
        tableQRDTO2.setId(tableQRDTO1.getId());
        assertThat(tableQRDTO1).isEqualTo(tableQRDTO2);
        tableQRDTO2.setId(2L);
        assertThat(tableQRDTO1).isNotEqualTo(tableQRDTO2);
        tableQRDTO1.setId(null);
        assertThat(tableQRDTO1).isNotEqualTo(tableQRDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tableQRMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tableQRMapper.fromId(null)).isNull();
    }
}
