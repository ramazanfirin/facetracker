package com.mastertek.web.rest;

import com.mastertek.FacetrackerApp;

import com.mastertek.domain.WhiteListPerson;
import com.mastertek.repository.WhiteListPersonRepository;
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
 * Test class for the WhiteListPersonResource REST controller.
 *
 * @see WhiteListPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
public class WhiteListPersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private WhiteListPersonRepository whiteListPersonRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWhiteListPersonMockMvc;

    private WhiteListPerson whiteListPerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WhiteListPersonResource whiteListPersonResource = new WhiteListPersonResource(whiteListPersonRepository);
        this.restWhiteListPersonMockMvc = MockMvcBuilders.standaloneSetup(whiteListPersonResource)
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
    public static WhiteListPerson createEntity(EntityManager em) {
        WhiteListPerson whiteListPerson = new WhiteListPerson()
            .name(DEFAULT_NAME);
        return whiteListPerson;
    }

    @Before
    public void initTest() {
        whiteListPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createWhiteListPerson() throws Exception {
        int databaseSizeBeforeCreate = whiteListPersonRepository.findAll().size();

        // Create the WhiteListPerson
        restWhiteListPersonMockMvc.perform(post("/api/white-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListPerson)))
            .andExpect(status().isCreated());

        // Validate the WhiteListPerson in the database
        List<WhiteListPerson> whiteListPersonList = whiteListPersonRepository.findAll();
        assertThat(whiteListPersonList).hasSize(databaseSizeBeforeCreate + 1);
        WhiteListPerson testWhiteListPerson = whiteListPersonList.get(whiteListPersonList.size() - 1);
        assertThat(testWhiteListPerson.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createWhiteListPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = whiteListPersonRepository.findAll().size();

        // Create the WhiteListPerson with an existing ID
        whiteListPerson.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWhiteListPersonMockMvc.perform(post("/api/white-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListPerson)))
            .andExpect(status().isBadRequest());

        // Validate the WhiteListPerson in the database
        List<WhiteListPerson> whiteListPersonList = whiteListPersonRepository.findAll();
        assertThat(whiteListPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = whiteListPersonRepository.findAll().size();
        // set the field null
        whiteListPerson.setName(null);

        // Create the WhiteListPerson, which fails.

        restWhiteListPersonMockMvc.perform(post("/api/white-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListPerson)))
            .andExpect(status().isBadRequest());

        List<WhiteListPerson> whiteListPersonList = whiteListPersonRepository.findAll();
        assertThat(whiteListPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWhiteListPeople() throws Exception {
        // Initialize the database
        whiteListPersonRepository.saveAndFlush(whiteListPerson);

        // Get all the whiteListPersonList
        restWhiteListPersonMockMvc.perform(get("/api/white-list-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(whiteListPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getWhiteListPerson() throws Exception {
        // Initialize the database
        whiteListPersonRepository.saveAndFlush(whiteListPerson);

        // Get the whiteListPerson
        restWhiteListPersonMockMvc.perform(get("/api/white-list-people/{id}", whiteListPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(whiteListPerson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWhiteListPerson() throws Exception {
        // Get the whiteListPerson
        restWhiteListPersonMockMvc.perform(get("/api/white-list-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWhiteListPerson() throws Exception {
        // Initialize the database
        whiteListPersonRepository.saveAndFlush(whiteListPerson);
        int databaseSizeBeforeUpdate = whiteListPersonRepository.findAll().size();

        // Update the whiteListPerson
        WhiteListPerson updatedWhiteListPerson = whiteListPersonRepository.findOne(whiteListPerson.getId());
        // Disconnect from session so that the updates on updatedWhiteListPerson are not directly saved in db
        em.detach(updatedWhiteListPerson);
        updatedWhiteListPerson
            .name(UPDATED_NAME);

        restWhiteListPersonMockMvc.perform(put("/api/white-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWhiteListPerson)))
            .andExpect(status().isOk());

        // Validate the WhiteListPerson in the database
        List<WhiteListPerson> whiteListPersonList = whiteListPersonRepository.findAll();
        assertThat(whiteListPersonList).hasSize(databaseSizeBeforeUpdate);
        WhiteListPerson testWhiteListPerson = whiteListPersonList.get(whiteListPersonList.size() - 1);
        assertThat(testWhiteListPerson.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingWhiteListPerson() throws Exception {
        int databaseSizeBeforeUpdate = whiteListPersonRepository.findAll().size();

        // Create the WhiteListPerson

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWhiteListPersonMockMvc.perform(put("/api/white-list-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whiteListPerson)))
            .andExpect(status().isCreated());

        // Validate the WhiteListPerson in the database
        List<WhiteListPerson> whiteListPersonList = whiteListPersonRepository.findAll();
        assertThat(whiteListPersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWhiteListPerson() throws Exception {
        // Initialize the database
        whiteListPersonRepository.saveAndFlush(whiteListPerson);
        int databaseSizeBeforeDelete = whiteListPersonRepository.findAll().size();

        // Get the whiteListPerson
        restWhiteListPersonMockMvc.perform(delete("/api/white-list-people/{id}", whiteListPerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WhiteListPerson> whiteListPersonList = whiteListPersonRepository.findAll();
        assertThat(whiteListPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhiteListPerson.class);
        WhiteListPerson whiteListPerson1 = new WhiteListPerson();
        whiteListPerson1.setId(1L);
        WhiteListPerson whiteListPerson2 = new WhiteListPerson();
        whiteListPerson2.setId(whiteListPerson1.getId());
        assertThat(whiteListPerson1).isEqualTo(whiteListPerson2);
        whiteListPerson2.setId(2L);
        assertThat(whiteListPerson1).isNotEqualTo(whiteListPerson2);
        whiteListPerson1.setId(null);
        assertThat(whiteListPerson1).isNotEqualTo(whiteListPerson2);
    }
}
