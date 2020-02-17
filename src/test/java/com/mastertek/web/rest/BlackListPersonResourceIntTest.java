package com.mastertek.web.rest;

import com.mastertek.FacetrackerApp;

import com.mastertek.domain.BlackListPerson;
import com.mastertek.repository.BlackListPersonRepository;
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
 * Test class for the BlackListPersonResource REST controller.
 *
 * @see BlackListPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
public class BlackListPersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BlackListPersonRepository blackListPersonRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBlackListPersonMockMvc;

    private BlackListPerson blackListPerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlackListPersonResource blackListPersonResource = new BlackListPersonResource(blackListPersonRepository);
        this.restBlackListPersonMockMvc = MockMvcBuilders.standaloneSetup(blackListPersonResource)
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
    public static BlackListPerson createEntity(EntityManager em) {
        BlackListPerson blackListPerson = new BlackListPerson()
            .name(DEFAULT_NAME);
        return blackListPerson;
    }

    @Before
    public void initTest() {
        blackListPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlackListPerson() throws Exception {
        int databaseSizeBeforeCreate = blackListPersonRepository.findAll().size();

        // Create the BlackListPerson
        restBlackListPersonMockMvc.perform(post("/api/black-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListPerson)))
            .andExpect(status().isCreated());

        // Validate the BlackListPerson in the database
        List<BlackListPerson> blackListPersonList = blackListPersonRepository.findAll();
        assertThat(blackListPersonList).hasSize(databaseSizeBeforeCreate + 1);
        BlackListPerson testBlackListPerson = blackListPersonList.get(blackListPersonList.size() - 1);
        assertThat(testBlackListPerson.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBlackListPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blackListPersonRepository.findAll().size();

        // Create the BlackListPerson with an existing ID
        blackListPerson.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlackListPersonMockMvc.perform(post("/api/black-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListPerson)))
            .andExpect(status().isBadRequest());

        // Validate the BlackListPerson in the database
        List<BlackListPerson> blackListPersonList = blackListPersonRepository.findAll();
        assertThat(blackListPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = blackListPersonRepository.findAll().size();
        // set the field null
        blackListPerson.setName(null);

        // Create the BlackListPerson, which fails.

        restBlackListPersonMockMvc.perform(post("/api/black-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListPerson)))
            .andExpect(status().isBadRequest());

        List<BlackListPerson> blackListPersonList = blackListPersonRepository.findAll();
        assertThat(blackListPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBlackListPeople() throws Exception {
        // Initialize the database
        blackListPersonRepository.saveAndFlush(blackListPerson);

        // Get all the blackListPersonList
        restBlackListPersonMockMvc.perform(get("/api/black-list-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blackListPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBlackListPerson() throws Exception {
        // Initialize the database
        blackListPersonRepository.saveAndFlush(blackListPerson);

        // Get the blackListPerson
        restBlackListPersonMockMvc.perform(get("/api/black-list-people/{id}", blackListPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blackListPerson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBlackListPerson() throws Exception {
        // Get the blackListPerson
        restBlackListPersonMockMvc.perform(get("/api/black-list-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlackListPerson() throws Exception {
        // Initialize the database
        blackListPersonRepository.saveAndFlush(blackListPerson);
        int databaseSizeBeforeUpdate = blackListPersonRepository.findAll().size();

        // Update the blackListPerson
        BlackListPerson updatedBlackListPerson = blackListPersonRepository.findOne(blackListPerson.getId());
        // Disconnect from session so that the updates on updatedBlackListPerson are not directly saved in db
        em.detach(updatedBlackListPerson);
        updatedBlackListPerson
            .name(UPDATED_NAME);

        restBlackListPersonMockMvc.perform(put("/api/black-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBlackListPerson)))
            .andExpect(status().isOk());

        // Validate the BlackListPerson in the database
        List<BlackListPerson> blackListPersonList = blackListPersonRepository.findAll();
        assertThat(blackListPersonList).hasSize(databaseSizeBeforeUpdate);
        BlackListPerson testBlackListPerson = blackListPersonList.get(blackListPersonList.size() - 1);
        assertThat(testBlackListPerson.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBlackListPerson() throws Exception {
        int databaseSizeBeforeUpdate = blackListPersonRepository.findAll().size();

        // Create the BlackListPerson

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlackListPersonMockMvc.perform(put("/api/black-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blackListPerson)))
            .andExpect(status().isCreated());

        // Validate the BlackListPerson in the database
        List<BlackListPerson> blackListPersonList = blackListPersonRepository.findAll();
        assertThat(blackListPersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBlackListPerson() throws Exception {
        // Initialize the database
        blackListPersonRepository.saveAndFlush(blackListPerson);
        int databaseSizeBeforeDelete = blackListPersonRepository.findAll().size();

        // Get the blackListPerson
        restBlackListPersonMockMvc.perform(delete("/api/black-list-people/{id}", blackListPerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BlackListPerson> blackListPersonList = blackListPersonRepository.findAll();
        assertThat(blackListPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlackListPerson.class);
        BlackListPerson blackListPerson1 = new BlackListPerson();
        blackListPerson1.setId(1L);
        BlackListPerson blackListPerson2 = new BlackListPerson();
        blackListPerson2.setId(blackListPerson1.getId());
        assertThat(blackListPerson1).isEqualTo(blackListPerson2);
        blackListPerson2.setId(2L);
        assertThat(blackListPerson1).isNotEqualTo(blackListPerson2);
        blackListPerson1.setId(null);
        assertThat(blackListPerson1).isNotEqualTo(blackListPerson2);
    }
}
