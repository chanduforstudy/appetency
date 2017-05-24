package com.fa.app.store.web.rest;

import com.fa.app.store.StoreApp;

import com.fa.app.store.config.SecurityBeanOverrideConfiguration;

import com.fa.app.store.domain.MenuItem;
import com.fa.app.store.repository.MenuItemRepository;
import com.fa.app.store.service.MenuItemService;
import com.fa.app.store.repository.search.MenuItemSearchRepository;
import com.fa.app.store.service.dto.MenuItemDTO;
import com.fa.app.store.service.mapper.MenuItemMapper;
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

import com.fa.app.store.domain.enumeration.Diet;
/**
 * Test class for the MenuItemResource REST controller.
 *
 * @see MenuItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreApp.class, SecurityBeanOverrideConfiguration.class})
public class MenuItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_PREPARATION_TIME = 1;
    private static final Integer UPDATED_PREPARATION_TIME = 2;

    private static final String DEFAULT_INGREDIENT = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Diet DEFAULT_DIET = Diet.VEG;
    private static final Diet UPDATED_DIET = Diet.NON_VEG;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuItemSearchRepository menuItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuItemResource menuItemResource = new MenuItemResource(menuItemService);
        this.restMenuItemMockMvc = MockMvcBuilders.standaloneSetup(menuItemResource)
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
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .preparationTime(DEFAULT_PREPARATION_TIME)
            .ingredient(DEFAULT_INGREDIENT)
            .imageUrl(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .diet(DEFAULT_DIET);
        return menuItem;
    }

    @Before
    public void initTest() {
        menuItemSearchRepository.deleteAll();
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);
        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMenuItem.getPreparationTime()).isEqualTo(DEFAULT_PREPARATION_TIME);
        assertThat(testMenuItem.getIngredient()).isEqualTo(DEFAULT_INGREDIENT);
        assertThat(testMenuItem.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenuItem.getDiet()).isEqualTo(DEFAULT_DIET);

        // Validate the MenuItem in Elasticsearch
        MenuItem menuItemEs = menuItemSearchRepository.findOne(testMenuItem.getId());
        assertThat(menuItemEs).isEqualToComparingFieldByField(testMenuItem);
    }

    @Test
    @Transactional
    public void createMenuItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem with an existing ID
        menuItem.setId(1L);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setName(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setPrice(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreparationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setPreparationTime(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].preparationTime").value(hasItem(DEFAULT_PREPARATION_TIME)))
            .andExpect(jsonPath("$.[*].ingredient").value(hasItem(DEFAULT_INGREDIENT.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].diet").value(hasItem(DEFAULT_DIET.toString())));
    }

    @Test
    @Transactional
    public void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.preparationTime").value(DEFAULT_PREPARATION_TIME))
            .andExpect(jsonPath("$.ingredient").value(DEFAULT_INGREDIENT.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.diet").value(DEFAULT_DIET.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);
        menuItemSearchRepository.save(menuItem);
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findOne(menuItem.getId());
        updatedMenuItem
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .ingredient(UPDATED_INGREDIENT)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .diet(UPDATED_DIET);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(updatedMenuItem);

        restMenuItemMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMenuItem.getPreparationTime()).isEqualTo(UPDATED_PREPARATION_TIME);
        assertThat(testMenuItem.getIngredient()).isEqualTo(UPDATED_INGREDIENT);
        assertThat(testMenuItem.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenuItem.getDiet()).isEqualTo(UPDATED_DIET);

        // Validate the MenuItem in Elasticsearch
        MenuItem menuItemEs = menuItemSearchRepository.findOne(testMenuItem.getId());
        assertThat(menuItemEs).isEqualToComparingFieldByField(testMenuItem);
    }

    @Test
    @Transactional
    public void updateNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMenuItemMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);
        menuItemSearchRepository.save(menuItem);
        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Get the menuItem
        restMenuItemMockMvc.perform(delete("/api/menu-items/{id}", menuItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean menuItemExistsInEs = menuItemSearchRepository.exists(menuItem.getId());
        assertThat(menuItemExistsInEs).isFalse();

        // Validate the database is empty
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);
        menuItemSearchRepository.save(menuItem);

        // Search the menuItem
        restMenuItemMockMvc.perform(get("/api/_search/menu-items?query=id:" + menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].preparationTime").value(hasItem(DEFAULT_PREPARATION_TIME)))
            .andExpect(jsonPath("$.[*].ingredient").value(hasItem(DEFAULT_INGREDIENT.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].diet").value(hasItem(DEFAULT_DIET.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItem.class);
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setId(1L);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(menuItem1.getId());
        assertThat(menuItem1).isEqualTo(menuItem2);
        menuItem2.setId(2L);
        assertThat(menuItem1).isNotEqualTo(menuItem2);
        menuItem1.setId(null);
        assertThat(menuItem1).isNotEqualTo(menuItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItemDTO.class);
        MenuItemDTO menuItemDTO1 = new MenuItemDTO();
        menuItemDTO1.setId(1L);
        MenuItemDTO menuItemDTO2 = new MenuItemDTO();
        assertThat(menuItemDTO1).isNotEqualTo(menuItemDTO2);
        menuItemDTO2.setId(menuItemDTO1.getId());
        assertThat(menuItemDTO1).isEqualTo(menuItemDTO2);
        menuItemDTO2.setId(2L);
        assertThat(menuItemDTO1).isNotEqualTo(menuItemDTO2);
        menuItemDTO1.setId(null);
        assertThat(menuItemDTO1).isNotEqualTo(menuItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(menuItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(menuItemMapper.fromId(null)).isNull();
    }
}
