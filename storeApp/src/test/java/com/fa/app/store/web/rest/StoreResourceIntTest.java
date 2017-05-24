package com.fa.app.store.web.rest;

import com.fa.app.store.StoreApp;

import com.fa.app.store.config.SecurityBeanOverrideConfiguration;

import com.fa.app.store.domain.Store;
import com.fa.app.store.repository.StoreRepository;
import com.fa.app.store.service.StoreService;
import com.fa.app.store.repository.search.StoreSearchRepository;
import com.fa.app.store.service.dto.StoreDTO;
import com.fa.app.store.service.mapper.StoreMapper;
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
 * Test class for the StoreResource REST controller.
 *
 * @see StoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreApp.class, SecurityBeanOverrideConfiguration.class})
public class StoreResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TIMINGS = "AAAAAAAAAA";
    private static final String UPDATED_TIMINGS = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SITE_URL = "BBBBBBBBBB";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreSearchRepository storeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStoreMockMvc;

    private Store store;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreResource storeResource = new StoreResource(storeService);
        this.restStoreMockMvc = MockMvcBuilders.standaloneSetup(storeResource)
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
    public static Store createEntity(EntityManager em) {
        Store store = new Store()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .timings(DEFAULT_TIMINGS)
            .website(DEFAULT_WEBSITE)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .faxNumber(DEFAULT_FAX_NUMBER)
            .siteUrl(DEFAULT_SITE_URL);
        return store;
    }

    @Before
    public void initTest() {
        storeSearchRepository.deleteAll();
        store = createEntity(em);
    }

    @Test
    @Transactional
    public void createStore() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate + 1);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStore.getTimings()).isEqualTo(DEFAULT_TIMINGS);
        assertThat(testStore.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testStore.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStore.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStore.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testStore.getFaxNumber()).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testStore.getSiteUrl()).isEqualTo(DEFAULT_SITE_URL);

        // Validate the Store in Elasticsearch
        Store storeEs = storeSearchRepository.findOne(testStore.getId());
        assertThat(storeEs).isEqualToComparingFieldByField(testStore);
    }

    @Test
    @Transactional
    public void createStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store with an existing ID
        store.setId(1L);
        StoreDTO storeDTO = storeMapper.toDto(store);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setCode(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setName(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStores() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].timings").value(hasItem(DEFAULT_TIMINGS.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL.toString())));
    }

    @Test
    @Transactional
    public void getStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(store.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.timings").value(DEFAULT_TIMINGS.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER.toString()))
            .andExpect(jsonPath("$.faxNumber").value(DEFAULT_FAX_NUMBER.toString()))
            .andExpect(jsonPath("$.siteUrl").value(DEFAULT_SITE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStore() throws Exception {
        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);
        storeSearchRepository.save(store);
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store
        Store updatedStore = storeRepository.findOne(store.getId());
        updatedStore
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .timings(UPDATED_TIMINGS)
            .website(UPDATED_WEBSITE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .faxNumber(UPDATED_FAX_NUMBER)
            .siteUrl(UPDATED_SITE_URL);
        StoreDTO storeDTO = storeMapper.toDto(updatedStore);

        restStoreMockMvc.perform(put("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStore.getTimings()).isEqualTo(UPDATED_TIMINGS);
        assertThat(testStore.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testStore.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStore.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStore.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testStore.getFaxNumber()).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testStore.getSiteUrl()).isEqualTo(UPDATED_SITE_URL);

        // Validate the Store in Elasticsearch
        Store storeEs = storeSearchRepository.findOne(testStore.getId());
        assertThat(storeEs).isEqualToComparingFieldByField(testStore);
    }

    @Test
    @Transactional
    public void updateNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStoreMockMvc.perform(put("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);
        storeSearchRepository.save(store);
        int databaseSizeBeforeDelete = storeRepository.findAll().size();

        // Get the store
        restStoreMockMvc.perform(delete("/api/stores/{id}", store.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean storeExistsInEs = storeSearchRepository.exists(store.getId());
        assertThat(storeExistsInEs).isFalse();

        // Validate the database is empty
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);
        storeSearchRepository.save(store);

        // Search the store
        restStoreMockMvc.perform(get("/api/_search/stores?query=id:" + store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].timings").value(hasItem(DEFAULT_TIMINGS.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Store.class);
        Store store1 = new Store();
        store1.setId(1L);
        Store store2 = new Store();
        store2.setId(store1.getId());
        assertThat(store1).isEqualTo(store2);
        store2.setId(2L);
        assertThat(store1).isNotEqualTo(store2);
        store1.setId(null);
        assertThat(store1).isNotEqualTo(store2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreDTO.class);
        StoreDTO storeDTO1 = new StoreDTO();
        storeDTO1.setId(1L);
        StoreDTO storeDTO2 = new StoreDTO();
        assertThat(storeDTO1).isNotEqualTo(storeDTO2);
        storeDTO2.setId(storeDTO1.getId());
        assertThat(storeDTO1).isEqualTo(storeDTO2);
        storeDTO2.setId(2L);
        assertThat(storeDTO1).isNotEqualTo(storeDTO2);
        storeDTO1.setId(null);
        assertThat(storeDTO1).isNotEqualTo(storeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeMapper.fromId(null)).isNull();
    }
}
