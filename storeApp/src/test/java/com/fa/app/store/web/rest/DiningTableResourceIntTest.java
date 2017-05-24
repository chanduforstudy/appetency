package com.fa.app.store.web.rest;

import com.fa.app.store.StoreApp;

import com.fa.app.store.config.SecurityBeanOverrideConfiguration;

import com.fa.app.store.domain.DiningTable;
import com.fa.app.store.repository.DiningTableRepository;
import com.fa.app.store.service.DiningTableService;
import com.fa.app.store.repository.search.DiningTableSearchRepository;
import com.fa.app.store.service.dto.DiningTableDTO;
import com.fa.app.store.service.mapper.DiningTableMapper;
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
 * Test class for the DiningTableResource REST controller.
 *
 * @see DiningTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreApp.class, SecurityBeanOverrideConfiguration.class})
public class DiningTableResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private DiningTableRepository diningTableRepository;

    @Autowired
    private DiningTableMapper diningTableMapper;

    @Autowired
    private DiningTableService diningTableService;

    @Autowired
    private DiningTableSearchRepository diningTableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiningTableMockMvc;

    private DiningTable diningTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiningTableResource diningTableResource = new DiningTableResource(diningTableService);
        this.restDiningTableMockMvc = MockMvcBuilders.standaloneSetup(diningTableResource)
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
    public static DiningTable createEntity(EntityManager em) {
        DiningTable diningTable = new DiningTable()
            .number(DEFAULT_NUMBER)
            .code(DEFAULT_CODE);
        return diningTable;
    }

    @Before
    public void initTest() {
        diningTableSearchRepository.deleteAll();
        diningTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiningTable() throws Exception {
        int databaseSizeBeforeCreate = diningTableRepository.findAll().size();

        // Create the DiningTable
        DiningTableDTO diningTableDTO = diningTableMapper.toDto(diningTable);
        restDiningTableMockMvc.perform(post("/api/dining-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diningTableDTO)))
            .andExpect(status().isCreated());

        // Validate the DiningTable in the database
        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeCreate + 1);
        DiningTable testDiningTable = diningTableList.get(diningTableList.size() - 1);
        assertThat(testDiningTable.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDiningTable.getCode()).isEqualTo(DEFAULT_CODE);

        // Validate the DiningTable in Elasticsearch
        DiningTable diningTableEs = diningTableSearchRepository.findOne(testDiningTable.getId());
        assertThat(diningTableEs).isEqualToComparingFieldByField(testDiningTable);
    }

    @Test
    @Transactional
    public void createDiningTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = diningTableRepository.findAll().size();

        // Create the DiningTable with an existing ID
        diningTable.setId(1L);
        DiningTableDTO diningTableDTO = diningTableMapper.toDto(diningTable);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiningTableMockMvc.perform(post("/api/dining-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diningTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = diningTableRepository.findAll().size();
        // set the field null
        diningTable.setNumber(null);

        // Create the DiningTable, which fails.
        DiningTableDTO diningTableDTO = diningTableMapper.toDto(diningTable);

        restDiningTableMockMvc.perform(post("/api/dining-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diningTableDTO)))
            .andExpect(status().isBadRequest());

        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = diningTableRepository.findAll().size();
        // set the field null
        diningTable.setCode(null);

        // Create the DiningTable, which fails.
        DiningTableDTO diningTableDTO = diningTableMapper.toDto(diningTable);

        restDiningTableMockMvc.perform(post("/api/dining-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diningTableDTO)))
            .andExpect(status().isBadRequest());

        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiningTables() throws Exception {
        // Initialize the database
        diningTableRepository.saveAndFlush(diningTable);

        // Get all the diningTableList
        restDiningTableMockMvc.perform(get("/api/dining-tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diningTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getDiningTable() throws Exception {
        // Initialize the database
        diningTableRepository.saveAndFlush(diningTable);

        // Get the diningTable
        restDiningTableMockMvc.perform(get("/api/dining-tables/{id}", diningTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diningTable.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiningTable() throws Exception {
        // Get the diningTable
        restDiningTableMockMvc.perform(get("/api/dining-tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiningTable() throws Exception {
        // Initialize the database
        diningTableRepository.saveAndFlush(diningTable);
        diningTableSearchRepository.save(diningTable);
        int databaseSizeBeforeUpdate = diningTableRepository.findAll().size();

        // Update the diningTable
        DiningTable updatedDiningTable = diningTableRepository.findOne(diningTable.getId());
        updatedDiningTable
            .number(UPDATED_NUMBER)
            .code(UPDATED_CODE);
        DiningTableDTO diningTableDTO = diningTableMapper.toDto(updatedDiningTable);

        restDiningTableMockMvc.perform(put("/api/dining-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diningTableDTO)))
            .andExpect(status().isOk());

        // Validate the DiningTable in the database
        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeUpdate);
        DiningTable testDiningTable = diningTableList.get(diningTableList.size() - 1);
        assertThat(testDiningTable.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDiningTable.getCode()).isEqualTo(UPDATED_CODE);

        // Validate the DiningTable in Elasticsearch
        DiningTable diningTableEs = diningTableSearchRepository.findOne(testDiningTable.getId());
        assertThat(diningTableEs).isEqualToComparingFieldByField(testDiningTable);
    }

    @Test
    @Transactional
    public void updateNonExistingDiningTable() throws Exception {
        int databaseSizeBeforeUpdate = diningTableRepository.findAll().size();

        // Create the DiningTable
        DiningTableDTO diningTableDTO = diningTableMapper.toDto(diningTable);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiningTableMockMvc.perform(put("/api/dining-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diningTableDTO)))
            .andExpect(status().isCreated());

        // Validate the DiningTable in the database
        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiningTable() throws Exception {
        // Initialize the database
        diningTableRepository.saveAndFlush(diningTable);
        diningTableSearchRepository.save(diningTable);
        int databaseSizeBeforeDelete = diningTableRepository.findAll().size();

        // Get the diningTable
        restDiningTableMockMvc.perform(delete("/api/dining-tables/{id}", diningTable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean diningTableExistsInEs = diningTableSearchRepository.exists(diningTable.getId());
        assertThat(diningTableExistsInEs).isFalse();

        // Validate the database is empty
        List<DiningTable> diningTableList = diningTableRepository.findAll();
        assertThat(diningTableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiningTable() throws Exception {
        // Initialize the database
        diningTableRepository.saveAndFlush(diningTable);
        diningTableSearchRepository.save(diningTable);

        // Search the diningTable
        restDiningTableMockMvc.perform(get("/api/_search/dining-tables?query=id:" + diningTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diningTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiningTable.class);
        DiningTable diningTable1 = new DiningTable();
        diningTable1.setId(1L);
        DiningTable diningTable2 = new DiningTable();
        diningTable2.setId(diningTable1.getId());
        assertThat(diningTable1).isEqualTo(diningTable2);
        diningTable2.setId(2L);
        assertThat(diningTable1).isNotEqualTo(diningTable2);
        diningTable1.setId(null);
        assertThat(diningTable1).isNotEqualTo(diningTable2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiningTableDTO.class);
        DiningTableDTO diningTableDTO1 = new DiningTableDTO();
        diningTableDTO1.setId(1L);
        DiningTableDTO diningTableDTO2 = new DiningTableDTO();
        assertThat(diningTableDTO1).isNotEqualTo(diningTableDTO2);
        diningTableDTO2.setId(diningTableDTO1.getId());
        assertThat(diningTableDTO1).isEqualTo(diningTableDTO2);
        diningTableDTO2.setId(2L);
        assertThat(diningTableDTO1).isNotEqualTo(diningTableDTO2);
        diningTableDTO1.setId(null);
        assertThat(diningTableDTO1).isNotEqualTo(diningTableDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(diningTableMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(diningTableMapper.fromId(null)).isNull();
    }
}
