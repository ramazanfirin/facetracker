package com.mastertek.web.rest;

import com.mastertek.FacetrackerApp;

import com.mastertek.domain.BlackListRecordItem;
import com.mastertek.repository.BlackListRecordItemRepository;
import com.mastertek.web.rest.errors.ExceptionTranslator;

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

import static com.mastertek.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlackListRecordItemResource REST controller.
 *
 * @see BlackListRecordItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
public class BlackListRecordItemResourceIntTest {

    @Autowired
    private BlackListRecordItemRepository blackListRecordItemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBlackListRecordItemMockMvc;

    private BlackListRecordItem blackListRecordItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlackListRecordItemResource blackListRecordItemResource = new BlackListRecordItemResource(blackListRecordItemRepository);
        this.restBlackListRecordItemMockMvc = MockMvcBuilders.standaloneSetup(blackListRecordItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlackListRecordItem createEntity(EntityManager em) {
        BlackListRecordItem blackListRecordItem = new BlackListRecordItem();
        return blackListRecordItem;
    }

    @Before
    public void initTest() {
        blackListRecordItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlackListRecordItem() throws Exception {
        int databaseSizeBeforeCreate = blackListRecordItemRepository.findAll().size();

        // Create the BlackListRecordItem
        restBlackListRecordItemMockMvc.perform(post("/api/black-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListRecordItem)))
            .andExpect(status().isCreated());

        // Validate the BlackListRecordItem in the database
        List<BlackListRecordItem> blackListRecordItemList = blackListRecordItemRepository.findAll();
        assertThat(blackListRecordItemList).hasSize(databaseSizeBeforeCreate + 1);
        BlackListRecordItem testBlackListRecordItem = blackListRecordItemList.get(blackListRecordItemList.size() - 1);
    }

    @Test
    @Transactional
    public void createBlackListRecordItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blackListRecordItemRepository.findAll().size();

        // Create the BlackListRecordItem with an existing ID
        blackListRecordItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlackListRecordItemMockMvc.perform(post("/api/black-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListRecordItem)))
            .andExpect(status().isBadRequest());

        // Validate the BlackListRecordItem in the database
        List<BlackListRecordItem> blackListRecordItemList = blackListRecordItemRepository.findAll();
        assertThat(blackListRecordItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBlackListRecordItems() throws Exception {
        // Initialize the database
        blackListRecordItemRepository.saveAndFlush(blackListRecordItem);

        // Get all the blackListRecordItemList
        restBlackListRecordItemMockMvc.perform(get("/api/black-list-record-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blackListRecordItem.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBlackListRecordItem() throws Exception {
        // Initialize the database
        blackListRecordItemRepository.saveAndFlush(blackListRecordItem);

        // Get the blackListRecordItem
        restBlackListRecordItemMockMvc.perform(get("/api/black-list-record-items/{id}", blackListRecordItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blackListRecordItem.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBlackListRecordItem() throws Exception {
        // Get the blackListRecordItem
        restBlackListRecordItemMockMvc.perform(get("/api/black-list-record-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlackListRecordItem() throws Exception {
        // Initialize the database
        blackListRecordItemRepository.saveAndFlush(blackListRecordItem);
        int databaseSizeBeforeUpdate = blackListRecordItemRepository.findAll().size();

        // Update the blackListRecordItem
        BlackListRecordItem updatedBlackListRecordItem = blackListRecordItemRepository.findOne(blackListRecordItem.getId());
        // Disconnect from session so that the updates on updatedBlackListRecordItem are not directly saved in db
        em.detach(updatedBlackListRecordItem);

        restBlackListRecordItemMockMvc.perform(put("/api/black-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBlackListRecordItem)))
            .andExpect(status().isOk());

        // Validate the BlackListRecordItem in the database
        List<BlackListRecordItem> blackListRecordItemList = blackListRecordItemRepository.findAll();
        assertThat(blackListRecordItemList).hasSize(databaseSizeBeforeUpdate);
        BlackListRecordItem testBlackListRecordItem = blackListRecordItemList.get(blackListRecordItemList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBlackListRecordItem() throws Exception {
        int databaseSizeBeforeUpdate = blackListRecordItemRepository.findAll().size();

        // Create the BlackListRecordItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlackListRecordItemMockMvc.perform(put("/api/black-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListRecordItem)))
            .andExpect(status().isCreated());

        // Validate the BlackListRecordItem in the database
        List<BlackListRecordItem> blackListRecordItemList = blackListRecordItemRepository.findAll();
        assertThat(blackListRecordItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBlackListRecordItem() throws Exception {
        // Initialize the database
        blackListRecordItemRepository.saveAndFlush(blackListRecordItem);
        int databaseSizeBeforeDelete = blackListRecordItemRepository.findAll().size();

        // Get the blackListRecordItem
        restBlackListRecordItemMockMvc.perform(delete("/api/black-list-record-items/{id}", blackListRecordItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BlackListRecordItem> blackListRecordItemList = blackListRecordItemRepository.findAll();
        assertThat(blackListRecordItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlackListRecordItem.class);
        BlackListRecordItem blackListRecordItem1 = new BlackListRecordItem();
        blackListRecordItem1.setId(1L);
        BlackListRecordItem blackListRecordItem2 = new BlackListRecordItem();
        blackListRecordItem2.setId(blackListRecordItem1.getId());
        assertThat(blackListRecordItem1).isEqualTo(blackListRecordItem2);
        blackListRecordItem2.setId(2L);
        assertThat(blackListRecordItem1).isNotEqualTo(blackListRecordItem2);
        blackListRecordItem1.setId(null);
        assertThat(blackListRecordItem1).isNotEqualTo(blackListRecordItem2);
    }
}
