package com.mastertek.web.rest;

import com.mastertek.FacetrackerApp;

import com.mastertek.domain.WhiteListRecordItem;
import com.mastertek.repository.WhiteListRecordItemRepository;
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
 * Test class for the WhiteListRecordItemResource REST controller.
 *
 * @see WhiteListRecordItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
public class WhiteListRecordItemResourceIntTest {

    @Autowired
    private WhiteListRecordItemRepository whiteListRecordItemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWhiteListRecordItemMockMvc;

    private WhiteListRecordItem whiteListRecordItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WhiteListRecordItemResource whiteListRecordItemResource = new WhiteListRecordItemResource(whiteListRecordItemRepository);
        this.restWhiteListRecordItemMockMvc = MockMvcBuilders.standaloneSetup(whiteListRecordItemResource)
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
    public static WhiteListRecordItem createEntity(EntityManager em) {
        WhiteListRecordItem whiteListRecordItem = new WhiteListRecordItem();
        return whiteListRecordItem;
    }

    @Before
    public void initTest() {
        whiteListRecordItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createWhiteListRecordItem() throws Exception {
        int databaseSizeBeforeCreate = whiteListRecordItemRepository.findAll().size();

        // Create the WhiteListRecordItem
        restWhiteListRecordItemMockMvc.perform(post("/api/white-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListRecordItem)))
            .andExpect(status().isCreated());

        // Validate the WhiteListRecordItem in the database
        List<WhiteListRecordItem> whiteListRecordItemList = whiteListRecordItemRepository.findAll();
        assertThat(whiteListRecordItemList).hasSize(databaseSizeBeforeCreate + 1);
        WhiteListRecordItem testWhiteListRecordItem = whiteListRecordItemList.get(whiteListRecordItemList.size() - 1);
    }

    @Test
    @Transactional
    public void createWhiteListRecordItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = whiteListRecordItemRepository.findAll().size();

        // Create the WhiteListRecordItem with an existing ID
        whiteListRecordItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWhiteListRecordItemMockMvc.perform(post("/api/white-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListRecordItem)))
            .andExpect(status().isBadRequest());

        // Validate the WhiteListRecordItem in the database
        List<WhiteListRecordItem> whiteListRecordItemList = whiteListRecordItemRepository.findAll();
        assertThat(whiteListRecordItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWhiteListRecordItems() throws Exception {
        // Initialize the database
        whiteListRecordItemRepository.saveAndFlush(whiteListRecordItem);

        // Get all the whiteListRecordItemList
        restWhiteListRecordItemMockMvc.perform(get("/api/white-list-record-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(whiteListRecordItem.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWhiteListRecordItem() throws Exception {
        // Initialize the database
        whiteListRecordItemRepository.saveAndFlush(whiteListRecordItem);

        // Get the whiteListRecordItem
        restWhiteListRecordItemMockMvc.perform(get("/api/white-list-record-items/{id}", whiteListRecordItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(whiteListRecordItem.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWhiteListRecordItem() throws Exception {
        // Get the whiteListRecordItem
        restWhiteListRecordItemMockMvc.perform(get("/api/white-list-record-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWhiteListRecordItem() throws Exception {
        // Initialize the database
        whiteListRecordItemRepository.saveAndFlush(whiteListRecordItem);
        int databaseSizeBeforeUpdate = whiteListRecordItemRepository.findAll().size();

        // Update the whiteListRecordItem
        WhiteListRecordItem updatedWhiteListRecordItem = whiteListRecordItemRepository.findOne(whiteListRecordItem.getId());
        // Disconnect from session so that the updates on updatedWhiteListRecordItem are not directly saved in db
        em.detach(updatedWhiteListRecordItem);

        restWhiteListRecordItemMockMvc.perform(put("/api/white-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWhiteListRecordItem)))
            .andExpect(status().isOk());

        // Validate the WhiteListRecordItem in the database
        List<WhiteListRecordItem> whiteListRecordItemList = whiteListRecordItemRepository.findAll();
        assertThat(whiteListRecordItemList).hasSize(databaseSizeBeforeUpdate);
        WhiteListRecordItem testWhiteListRecordItem = whiteListRecordItemList.get(whiteListRecordItemList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingWhiteListRecordItem() throws Exception {
        int databaseSizeBeforeUpdate = whiteListRecordItemRepository.findAll().size();

        // Create the WhiteListRecordItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWhiteListRecordItemMockMvc.perform(put("/api/white-list-record-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListRecordItem)))
            .andExpect(status().isCreated());

        // Validate the WhiteListRecordItem in the database
        List<WhiteListRecordItem> whiteListRecordItemList = whiteListRecordItemRepository.findAll();
        assertThat(whiteListRecordItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWhiteListRecordItem() throws Exception {
        // Initialize the database
        whiteListRecordItemRepository.saveAndFlush(whiteListRecordItem);
        int databaseSizeBeforeDelete = whiteListRecordItemRepository.findAll().size();

        // Get the whiteListRecordItem
        restWhiteListRecordItemMockMvc.perform(delete("/api/white-list-record-items/{id}", whiteListRecordItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WhiteListRecordItem> whiteListRecordItemList = whiteListRecordItemRepository.findAll();
        assertThat(whiteListRecordItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhiteListRecordItem.class);
        WhiteListRecordItem whiteListRecordItem1 = new WhiteListRecordItem();
        whiteListRecordItem1.setId(1L);
        WhiteListRecordItem whiteListRecordItem2 = new WhiteListRecordItem();
        whiteListRecordItem2.setId(whiteListRecordItem1.getId());
        assertThat(whiteListRecordItem1).isEqualTo(whiteListRecordItem2);
        whiteListRecordItem2.setId(2L);
        assertThat(whiteListRecordItem1).isNotEqualTo(whiteListRecordItem2);
        whiteListRecordItem1.setId(null);
        assertThat(whiteListRecordItem1).isNotEqualTo(whiteListRecordItem2);
    }
}
