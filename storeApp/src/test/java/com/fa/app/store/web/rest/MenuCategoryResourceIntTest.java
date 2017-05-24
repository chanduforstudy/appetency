package com.fa.app.store.web.rest;

import com.fa.app.store.StoreApp;

import com.fa.app.store.config.SecurityBeanOverrideConfiguration;

import com.fa.app.store.domain.MenuCategory;
import com.fa.app.store.repository.MenuCategoryRepository;
import com.fa.app.store.service.MenuCategoryService;
import com.fa.app.store.repository.search.MenuCategorySearchRepository;
import com.fa.app.store.service.dto.MenuCategoryDTO;
import com.fa.app.store.service.mapper.MenuCategoryMapper;
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
 * Test class for the MenuCategoryResource REST controller.
 *
 * @see MenuCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreApp.class, SecurityBeanOverrideConfiguration.class})
public class MenuCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private MenuCategoryMapper menuCategoryMapper;

    @Autowired
    private MenuCategoryService menuCategoryService;

    @Autowired
    private MenuCategorySearchRepository menuCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMenuCategoryMockMvc;

    private MenuCategory menuCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuCategoryResource menuCategoryResource = new MenuCategoryResource(menuCategoryService);
        this.restMenuCategoryMockMvc = MockMvcBuilders.standaloneSetup(menuCategoryResource)
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
    public static MenuCategory createEntity(EntityManager em) {
        MenuCategory menuCategory = new MenuCategory()
            .name(DEFAULT_NAME);
        return menuCategory;
    }

    @Before
    public void initTest() {
        menuCategorySearchRepository.deleteAll();
        menuCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuCategory() throws Exception {
        int databaseSizeBeforeCreate = menuCategoryRepository.findAll().size();

        // Create the MenuCategory
        MenuCategoryDTO menuCategoryDTO = menuCategoryMapper.toDto(menuCategory);
        restMenuCategoryMockMvc.perform(post("/api/menu-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuCategory in the database
        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAll();
        assertThat(menuCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        MenuCategory testMenuCategory = menuCategoryList.get(menuCategoryList.size() - 1);
        assertThat(testMenuCategory.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the MenuCategory in Elasticsearch
        MenuCategory menuCategoryEs = menuCategorySearchRepository.findOne(testMenuCategory.getId());
        assertThat(menuCategoryEs).isEqualToComparingFieldByField(testMenuCategory);
    }

    @Test
    @Transactional
    public void createMenuCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuCategoryRepository.findAll().size();

        // Create the MenuCategory with an existing ID
        menuCategory.setId(1L);
        MenuCategoryDTO menuCategoryDTO = menuCategoryMapper.toDto(menuCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuCategoryMockMvc.perform(post("/api/menu-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAll();
        assertThat(menuCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuCategoryRepository.findAll().size();
        // set the field null
        menuCategory.setName(null);

        // Create the MenuCategory, which fails.
        MenuCategoryDTO menuCategoryDTO = menuCategoryMapper.toDto(menuCategory);

        restMenuCategoryMockMvc.perform(post("/api/menu-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAll();
        assertThat(menuCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenuCategories() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        // Get all the menuCategoryList
        restMenuCategoryMockMvc.perform(get("/api/menu-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        // Get the menuCategory
        restMenuCategoryMockMvc.perform(get("/api/menu-categories/{id}", menuCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMenuCategory() throws Exception {
        // Get the menuCategory
        restMenuCategoryMockMvc.perform(get("/api/menu-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);
        menuCategorySearchRepository.save(menuCategory);
        int databaseSizeBeforeUpdate = menuCategoryRepository.findAll().size();

        // Update the menuCategory
        MenuCategory updatedMenuCategory = menuCategoryRepository.findOne(menuCategory.getId());
        updatedMenuCategory
            .name(UPDATED_NAME);
        MenuCategoryDTO menuCategoryDTO = menuCategoryMapper.toDto(updatedMenuCategory);

        restMenuCategoryMockMvc.perform(put("/api/menu-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the MenuCategory in the database
        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAll();
        assertThat(menuCategoryList).hasSize(databaseSizeBeforeUpdate);
        MenuCategory testMenuCategory = menuCategoryList.get(menuCategoryList.size() - 1);
        assertThat(testMenuCategory.getName()).isEqualTo(UPDATED_NAME);

        // Validate the MenuCategory in Elasticsearch
        MenuCategory menuCategoryEs = menuCategorySearchRepository.findOne(testMenuCategory.getId());
        assertThat(menuCategoryEs).isEqualToComparingFieldByField(testMenuCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingMenuCategory() throws Exception {
        int databaseSizeBeforeUpdate = menuCategoryRepository.findAll().size();

        // Create the MenuCategory
        MenuCategoryDTO menuCategoryDTO = menuCategoryMapper.toDto(menuCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMenuCategoryMockMvc.perform(put("/api/menu-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuCategory in the database
        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAll();
        assertThat(menuCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);
        menuCategorySearchRepository.save(menuCategory);
        int databaseSizeBeforeDelete = menuCategoryRepository.findAll().size();

        // Get the menuCategory
        restMenuCategoryMockMvc.perform(delete("/api/menu-categories/{id}", menuCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean menuCategoryExistsInEs = menuCategorySearchRepository.exists(menuCategory.getId());
        assertThat(menuCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAll();
        assertThat(menuCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);
        menuCategorySearchRepository.save(menuCategory);

        // Search the menuCategory
        restMenuCategoryMockMvc.perform(get("/api/_search/menu-categories?query=id:" + menuCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuCategory.class);
        MenuCategory menuCategory1 = new MenuCategory();
        menuCategory1.setId(1L);
        MenuCategory menuCategory2 = new MenuCategory();
        menuCategory2.setId(menuCategory1.getId());
        assertThat(menuCategory1).isEqualTo(menuCategory2);
        menuCategory2.setId(2L);
        assertThat(menuCategory1).isNotEqualTo(menuCategory2);
        menuCategory1.setId(null);
        assertThat(menuCategory1).isNotEqualTo(menuCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuCategoryDTO.class);
        MenuCategoryDTO menuCategoryDTO1 = new MenuCategoryDTO();
        menuCategoryDTO1.setId(1L);
        MenuCategoryDTO menuCategoryDTO2 = new MenuCategoryDTO();
        assertThat(menuCategoryDTO1).isNotEqualTo(menuCategoryDTO2);
        menuCategoryDTO2.setId(menuCategoryDTO1.getId());
        assertThat(menuCategoryDTO1).isEqualTo(menuCategoryDTO2);
        menuCategoryDTO2.setId(2L);
        assertThat(menuCategoryDTO1).isNotEqualTo(menuCategoryDTO2);
        menuCategoryDTO1.setId(null);
        assertThat(menuCategoryDTO1).isNotEqualTo(menuCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(menuCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(menuCategoryMapper.fromId(null)).isNull();
    }
}
