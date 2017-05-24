package com.fa.app.store.web.rest;

import com.fa.app.store.StoreApp;

import com.fa.app.store.config.SecurityBeanOverrideConfiguration;

import com.fa.app.store.domain.StoreGroup;
import com.fa.app.store.repository.StoreGroupRepository;
import com.fa.app.store.service.StoreGroupService;
import com.fa.app.store.repository.search.StoreGroupSearchRepository;
import com.fa.app.store.service.dto.StoreGroupDTO;
import com.fa.app.store.service.mapper.StoreGroupMapper;
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
 * Test class for the StoreGroupResource REST controller.
 *
 * @see StoreGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreApp.class, SecurityBeanOverrideConfiguration.class})
public class StoreGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SITE_URL = "BBBBBBBBBB";

    @Autowired
    private StoreGroupRepository storeGroupRepository;

    @Autowired
    private StoreGroupMapper storeGroupMapper;

    @Autowired
    private StoreGroupService storeGroupService;

    @Autowired
    private StoreGroupSearchRepository storeGroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStoreGroupMockMvc;

    private StoreGroup storeGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreGroupResource storeGroupResource = new StoreGroupResource(storeGroupService);
        this.restStoreGroupMockMvc = MockMvcBuilders.standaloneSetup(storeGroupResource)
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
    public static StoreGroup createEntity(EntityManager em) {
        StoreGroup storeGroup = new StoreGroup()
            .name(DEFAULT_NAME)
            .siteUrl(DEFAULT_SITE_URL);
        return storeGroup;
    }

    @Before
    public void initTest() {
        storeGroupSearchRepository.deleteAll();
        storeGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreGroup() throws Exception {
        int databaseSizeBeforeCreate = storeGroupRepository.findAll().size();

        // Create the StoreGroup
        StoreGroupDTO storeGroupDTO = storeGroupMapper.toDto(storeGroup);
        restStoreGroupMockMvc.perform(post("/api/store-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreGroup in the database
        List<StoreGroup> storeGroupList = storeGroupRepository.findAll();
        assertThat(storeGroupList).hasSize(databaseSizeBeforeCreate + 1);
        StoreGroup testStoreGroup = storeGroupList.get(storeGroupList.size() - 1);
        assertThat(testStoreGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStoreGroup.getSiteUrl()).isEqualTo(DEFAULT_SITE_URL);

        // Validate the StoreGroup in Elasticsearch
        StoreGroup storeGroupEs = storeGroupSearchRepository.findOne(testStoreGroup.getId());
        assertThat(storeGroupEs).isEqualToComparingFieldByField(testStoreGroup);
    }

    @Test
    @Transactional
    public void createStoreGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeGroupRepository.findAll().size();

        // Create the StoreGroup with an existing ID
        storeGroup.setId(1L);
        StoreGroupDTO storeGroupDTO = storeGroupMapper.toDto(storeGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreGroupMockMvc.perform(post("/api/store-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StoreGroup> storeGroupList = storeGroupRepository.findAll();
        assertThat(storeGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeGroupRepository.findAll().size();
        // set the field null
        storeGroup.setName(null);

        // Create the StoreGroup, which fails.
        StoreGroupDTO storeGroupDTO = storeGroupMapper.toDto(storeGroup);

        restStoreGroupMockMvc.perform(post("/api/store-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeGroupDTO)))
            .andExpect(status().isBadRequest());

        List<StoreGroup> storeGroupList = storeGroupRepository.findAll();
        assertThat(storeGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreGroups() throws Exception {
        // Initialize the database
        storeGroupRepository.saveAndFlush(storeGroup);

        // Get all the storeGroupList
        restStoreGroupMockMvc.perform(get("/api/store-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL.toString())));
    }

    @Test
    @Transactional
    public void getStoreGroup() throws Exception {
        // Initialize the database
        storeGroupRepository.saveAndFlush(storeGroup);

        // Get the storeGroup
        restStoreGroupMockMvc.perform(get("/api/store-groups/{id}", storeGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storeGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.siteUrl").value(DEFAULT_SITE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreGroup() throws Exception {
        // Get the storeGroup
        restStoreGroupMockMvc.perform(get("/api/store-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreGroup() throws Exception {
        // Initialize the database
        storeGroupRepository.saveAndFlush(storeGroup);
        storeGroupSearchRepository.save(storeGroup);
        int databaseSizeBeforeUpdate = storeGroupRepository.findAll().size();

        // Update the storeGroup
        StoreGroup updatedStoreGroup = storeGroupRepository.findOne(storeGroup.getId());
        updatedStoreGroup
            .name(UPDATED_NAME)
            .siteUrl(UPDATED_SITE_URL);
        StoreGroupDTO storeGroupDTO = storeGroupMapper.toDto(updatedStoreGroup);

        restStoreGroupMockMvc.perform(put("/api/store-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeGroupDTO)))
            .andExpect(status().isOk());

        // Validate the StoreGroup in the database
        List<StoreGroup> storeGroupList = storeGroupRepository.findAll();
        assertThat(storeGroupList).hasSize(databaseSizeBeforeUpdate);
        StoreGroup testStoreGroup = storeGroupList.get(storeGroupList.size() - 1);
        assertThat(testStoreGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStoreGroup.getSiteUrl()).isEqualTo(UPDATED_SITE_URL);

        // Validate the StoreGroup in Elasticsearch
        StoreGroup storeGroupEs = storeGroupSearchRepository.findOne(testStoreGroup.getId());
        assertThat(storeGroupEs).isEqualToComparingFieldByField(testStoreGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreGroup() throws Exception {
        int databaseSizeBeforeUpdate = storeGroupRepository.findAll().size();

        // Create the StoreGroup
        StoreGroupDTO storeGroupDTO = storeGroupMapper.toDto(storeGroup);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStoreGroupMockMvc.perform(put("/api/store-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreGroup in the database
        List<StoreGroup> storeGroupList = storeGroupRepository.findAll();
        assertThat(storeGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStoreGroup() throws Exception {
        // Initialize the database
        storeGroupRepository.saveAndFlush(storeGroup);
        storeGroupSearchRepository.save(storeGroup);
        int databaseSizeBeforeDelete = storeGroupRepository.findAll().size();

        // Get the storeGroup
        restStoreGroupMockMvc.perform(delete("/api/store-groups/{id}", storeGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean storeGroupExistsInEs = storeGroupSearchRepository.exists(storeGroup.getId());
        assertThat(storeGroupExistsInEs).isFalse();

        // Validate the database is empty
        List<StoreGroup> storeGroupList = storeGroupRepository.findAll();
        assertThat(storeGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStoreGroup() throws Exception {
        // Initialize the database
        storeGroupRepository.saveAndFlush(storeGroup);
        storeGroupSearchRepository.save(storeGroup);

        // Search the storeGroup
        restStoreGroupMockMvc.perform(get("/api/_search/store-groups?query=id:" + storeGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreGroup.class);
        StoreGroup storeGroup1 = new StoreGroup();
        storeGroup1.setId(1L);
        StoreGroup storeGroup2 = new StoreGroup();
        storeGroup2.setId(storeGroup1.getId());
        assertThat(storeGroup1).isEqualTo(storeGroup2);
        storeGroup2.setId(2L);
        assertThat(storeGroup1).isNotEqualTo(storeGroup2);
        storeGroup1.setId(null);
        assertThat(storeGroup1).isNotEqualTo(storeGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreGroupDTO.class);
        StoreGroupDTO storeGroupDTO1 = new StoreGroupDTO();
        storeGroupDTO1.setId(1L);
        StoreGroupDTO storeGroupDTO2 = new StoreGroupDTO();
        assertThat(storeGroupDTO1).isNotEqualTo(storeGroupDTO2);
        storeGroupDTO2.setId(storeGroupDTO1.getId());
        assertThat(storeGroupDTO1).isEqualTo(storeGroupDTO2);
        storeGroupDTO2.setId(2L);
        assertThat(storeGroupDTO1).isNotEqualTo(storeGroupDTO2);
        storeGroupDTO1.setId(null);
        assertThat(storeGroupDTO1).isNotEqualTo(storeGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeGroupMapper.fromId(null)).isNull();
    }
}
