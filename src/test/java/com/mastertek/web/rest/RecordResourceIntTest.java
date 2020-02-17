package com.mastertek.web.rest;

import com.mastertek.FacetrackerApp;

import com.mastertek.domain.Record;
import com.mastertek.repository.RecordRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.mastertek.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mastertek.domain.enumeration.RecordStatus;
/**
 * Test class for the RecordResource REST controller.
 *
 * @see RecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
public class RecordResourceIntTest {

    private static final Instant DEFAULT_INSERT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSERT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_FILE_SENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FILE_SENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FILE_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FILE_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESS_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESS_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESS_FINISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESS_FINISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final RecordStatus DEFAULT_STATUS = RecordStatus.PROCESSING_STARTED;
    private static final RecordStatus UPDATED_STATUS = RecordStatus.PROCESSING_FINISHED;

    private static final byte[] DEFAULT_AFID = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AFID = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_AFID_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AFID_CONTENT_TYPE = "image/png";

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRecordMockMvc;

    private Record record;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecordResource recordResource = new RecordResource(recordRepository);
        this.restRecordMockMvc = MockMvcBuilders.standaloneSetup(recordResource)
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
    public static Record createEntity(EntityManager em) {
        Record record = new Record()
            .insert(DEFAULT_INSERT)
            .path(DEFAULT_PATH)
            .fileSentDate(DEFAULT_FILE_SENT_DATE)
            .fileCreationDate(DEFAULT_FILE_CREATION_DATE)
            .processStartDate(DEFAULT_PROCESS_START_DATE)
            .processFinishDate(DEFAULT_PROCESS_FINISH_DATE)
            .status(DEFAULT_STATUS)
            .afid(DEFAULT_AFID)
            .afidContentType(DEFAULT_AFID_CONTENT_TYPE);
        return record;
    }

    @Before
    public void initTest() {
        record = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecord() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        // Create the Record
        restRecordMockMvc.perform(post("/api/records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isCreated());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + 1);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getInsert()).isEqualTo(DEFAULT_INSERT);
        assertThat(testRecord.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testRecord.getFileSentDate()).isEqualTo(DEFAULT_FILE_SENT_DATE);
        assertThat(testRecord.getFileCreationDate()).isEqualTo(DEFAULT_FILE_CREATION_DATE);
        assertThat(testRecord.getProcessStartDate()).isEqualTo(DEFAULT_PROCESS_START_DATE);
        assertThat(testRecord.getProcessFinishDate()).isEqualTo(DEFAULT_PROCESS_FINISH_DATE);
        assertThat(testRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRecord.getAfid()).isEqualTo(DEFAULT_AFID);
        assertThat(testRecord.getAfidContentType()).isEqualTo(DEFAULT_AFID_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        // Create the Record with an existing ID
        record.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordMockMvc.perform(post("/api/records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRecords() throws Exception {
        // Initialize the database
        recordRepository.saveAndFlush(record);

        // Get all the recordList
        restRecordMockMvc.perform(get("/api/records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(record.getId().intValue())))
            .andExpect(jsonPath("$.[*].insert").value(hasItem(DEFAULT_INSERT.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileSentDate").value(hasItem(DEFAULT_FILE_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileCreationDate").value(hasItem(DEFAULT_FILE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].processStartDate").value(hasItem(DEFAULT_PROCESS_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].processFinishDate").value(hasItem(DEFAULT_PROCESS_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].afidContentType").value(hasItem(DEFAULT_AFID_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].afid").value(hasItem(Base64Utils.encodeToString(DEFAULT_AFID))));
    }

    @Test
    @Transactional
    public void getRecord() throws Exception {
        // Initialize the database
        recordRepository.saveAndFlush(record);

        // Get the record
        restRecordMockMvc.perform(get("/api/records/{id}", record.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(record.getId().intValue()))
            .andExpect(jsonPath("$.insert").value(DEFAULT_INSERT.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.fileSentDate").value(DEFAULT_FILE_SENT_DATE.toString()))
            .andExpect(jsonPath("$.fileCreationDate").value(DEFAULT_FILE_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.processStartDate").value(DEFAULT_PROCESS_START_DATE.toString()))
            .andExpect(jsonPath("$.processFinishDate").value(DEFAULT_PROCESS_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.afidContentType").value(DEFAULT_AFID_CONTENT_TYPE))
            .andExpect(jsonPath("$.afid").value(Base64Utils.encodeToString(DEFAULT_AFID)));
    }

    @Test
    @Transactional
    public void getNonExistingRecord() throws Exception {
        // Get the record
        restRecordMockMvc.perform(get("/api/records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecord() throws Exception {
        // Initialize the database
        recordRepository.saveAndFlush(record);
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record
        Record updatedRecord = recordRepository.findOne(record.getId());
        // Disconnect from session so that the updates on updatedRecord are not directly saved in db
        em.detach(updatedRecord);
        updatedRecord
            .insert(UPDATED_INSERT)
            .path(UPDATED_PATH)
            .fileSentDate(UPDATED_FILE_SENT_DATE)
            .fileCreationDate(UPDATED_FILE_CREATION_DATE)
            .processStartDate(UPDATED_PROCESS_START_DATE)
            .processFinishDate(UPDATED_PROCESS_FINISH_DATE)
            .status(UPDATED_STATUS)
            .afid(UPDATED_AFID)
            .afidContentType(UPDATED_AFID_CONTENT_TYPE);

        restRecordMockMvc.perform(put("/api/records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecord)))
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getInsert()).isEqualTo(UPDATED_INSERT);
        assertThat(testRecord.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testRecord.getFileSentDate()).isEqualTo(UPDATED_FILE_SENT_DATE);
        assertThat(testRecord.getFileCreationDate()).isEqualTo(UPDATED_FILE_CREATION_DATE);
        assertThat(testRecord.getProcessStartDate()).isEqualTo(UPDATED_PROCESS_START_DATE);
        assertThat(testRecord.getProcessFinishDate()).isEqualTo(UPDATED_PROCESS_FINISH_DATE);
        assertThat(testRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRecord.getAfid()).isEqualTo(UPDATED_AFID);
        assertThat(testRecord.getAfidContentType()).isEqualTo(UPDATED_AFID_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Create the Record

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRecordMockMvc.perform(put("/api/records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(record)))
            .andExpect(status().isCreated());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRecord() throws Exception {
        // Initialize the database
        recordRepository.saveAndFlush(record);
        int databaseSizeBeforeDelete = recordRepository.findAll().size();

        // Get the record
        restRecordMockMvc.perform(delete("/api/records/{id}", record.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Record.class);
        Record record1 = new Record();
        record1.setId(1L);
        Record record2 = new Record();
        record2.setId(record1.getId());
        assertThat(record1).isEqualTo(record2);
        record2.setId(2L);
        assertThat(record1).isNotEqualTo(record2);
        record1.setId(null);
        assertThat(record1).isNotEqualTo(record2);
    }
}
