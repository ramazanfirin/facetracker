package com.mastertek.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mastertek.FacetrackerApp;
import com.mastertek.domain.Device;
import com.mastertek.domain.Image;
import com.mastertek.domain.Person;
import com.mastertek.domain.Record;
import com.mastertek.domain.RecordSense;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.PersonRepository;
import com.mastertek.repository.RecordSenseRepository;
import com.mastertek.web.rest.errors.ExceptionTranslator;
import com.mastertek.web.rest.vm.RecordReportVM;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.mastertek.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mastertek.domain.enumeration.DeviceType;
import com.mastertek.domain.enumeration.RecordStatus;
/**
 * Test class for the RecordSenseResource REST controller.
 *
 * @see RecordSenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
public class RecordSenseResourceIntTest {

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

    private static final RecordStatus DEFAULT_STATUS = RecordStatus.NO_FACE_DETECTED;
    private static final RecordStatus UPDATED_STATUS = RecordStatus.NO_AFID_DETECTED;

    private static final byte[] DEFAULT_AFID = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AFID = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_AFID_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AFID_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_PROCESSED = false;
    private static final Boolean UPDATED_IS_PROCESSED = true;

    private static final Float DEFAULT_SIMILARITY = 1F;
    private static final Float UPDATED_SIMILARITY = 2F;

    @Autowired
    private RecordSenseRepository recordSenseRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRecordSenseMockMvc;

    private RecordSense recordSense;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecordSenseResource recordSenseResource = new RecordSenseResource(recordSenseRepository,personRepository);
        this.restRecordSenseMockMvc = MockMvcBuilders.standaloneSetup(recordSenseResource)
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
    public static RecordSense createEntity(EntityManager em) {
        RecordSense recordSense = new RecordSense()
            .insert(DEFAULT_INSERT)
            .path(DEFAULT_PATH)
            .fileSentDate(DEFAULT_FILE_SENT_DATE)
            .fileCreationDate(DEFAULT_FILE_CREATION_DATE)
            .processStartDate(DEFAULT_PROCESS_START_DATE)
            .processFinishDate(DEFAULT_PROCESS_FINISH_DATE)
            .status(DEFAULT_STATUS)
            .afid(DEFAULT_AFID)
            .afidContentType(DEFAULT_AFID_CONTENT_TYPE)
            .isProcessed(DEFAULT_IS_PROCESSED)
            .similarity(DEFAULT_SIMILARITY);
        return recordSense;
    }

    @Before
    public void initTest() {
        recordSense = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecordSense() throws Exception {
        int databaseSizeBeforeCreate = recordSenseRepository.findAll().size();

        // Create the RecordSense
        restRecordSenseMockMvc.perform(post("/api/record-senses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recordSense)))
            .andExpect(status().isCreated());

        // Validate the RecordSense in the database
        List<RecordSense> recordSenseList = recordSenseRepository.findAll();
        assertThat(recordSenseList).hasSize(databaseSizeBeforeCreate + 1);
        RecordSense testRecordSense = recordSenseList.get(recordSenseList.size() - 1);
        assertThat(testRecordSense.getInsert()).isEqualTo(DEFAULT_INSERT);
        assertThat(testRecordSense.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testRecordSense.getFileSentDate()).isEqualTo(DEFAULT_FILE_SENT_DATE);
        assertThat(testRecordSense.getFileCreationDate()).isEqualTo(DEFAULT_FILE_CREATION_DATE);
        assertThat(testRecordSense.getProcessStartDate()).isEqualTo(DEFAULT_PROCESS_START_DATE);
        assertThat(testRecordSense.getProcessFinishDate()).isEqualTo(DEFAULT_PROCESS_FINISH_DATE);
        assertThat(testRecordSense.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRecordSense.getAfid()).isEqualTo(DEFAULT_AFID);
        assertThat(testRecordSense.getAfidContentType()).isEqualTo(DEFAULT_AFID_CONTENT_TYPE);
        assertThat(testRecordSense.isIsProcessed()).isEqualTo(DEFAULT_IS_PROCESSED);
        assertThat(testRecordSense.getSimilarity()).isEqualTo(DEFAULT_SIMILARITY);
    }

    @Test
    @Transactional
    public void createRecordSenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recordSenseRepository.findAll().size();

        // Create the RecordSense with an existing ID
        recordSense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordSenseMockMvc.perform(post("/api/record-senses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recordSense)))
            .andExpect(status().isBadRequest());

        // Validate the RecordSense in the database
        List<RecordSense> recordSenseList = recordSenseRepository.findAll();
        assertThat(recordSenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRecordSenses() throws Exception {
        // Initialize the database
        recordSenseRepository.saveAndFlush(recordSense);

        // Get all the recordSenseList
        restRecordSenseMockMvc.perform(get("/api/record-senses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recordSense.getId().intValue())))
            .andExpect(jsonPath("$.[*].insert").value(hasItem(DEFAULT_INSERT.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileSentDate").value(hasItem(DEFAULT_FILE_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileCreationDate").value(hasItem(DEFAULT_FILE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].processStartDate").value(hasItem(DEFAULT_PROCESS_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].processFinishDate").value(hasItem(DEFAULT_PROCESS_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].afidContentType").value(hasItem(DEFAULT_AFID_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].afid").value(hasItem(Base64Utils.encodeToString(DEFAULT_AFID))))
            .andExpect(jsonPath("$.[*].isProcessed").value(hasItem(DEFAULT_IS_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].similarity").value(hasItem(DEFAULT_SIMILARITY.doubleValue())));
    }

    @Test
    @Transactional
    public void getRecordSense() throws Exception {
        // Initialize the database
        recordSenseRepository.saveAndFlush(recordSense);

        // Get the recordSense
        restRecordSenseMockMvc.perform(get("/api/record-senses/{id}", recordSense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recordSense.getId().intValue()))
            .andExpect(jsonPath("$.insert").value(DEFAULT_INSERT.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.fileSentDate").value(DEFAULT_FILE_SENT_DATE.toString()))
            .andExpect(jsonPath("$.fileCreationDate").value(DEFAULT_FILE_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.processStartDate").value(DEFAULT_PROCESS_START_DATE.toString()))
            .andExpect(jsonPath("$.processFinishDate").value(DEFAULT_PROCESS_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.afidContentType").value(DEFAULT_AFID_CONTENT_TYPE))
            .andExpect(jsonPath("$.afid").value(Base64Utils.encodeToString(DEFAULT_AFID)))
            .andExpect(jsonPath("$.isProcessed").value(DEFAULT_IS_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.similarity").value(DEFAULT_SIMILARITY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecordSense() throws Exception {
        // Get the recordSense
        restRecordSenseMockMvc.perform(get("/api/record-senses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecordSense() throws Exception {
        // Initialize the database
        recordSenseRepository.saveAndFlush(recordSense);
        int databaseSizeBeforeUpdate = recordSenseRepository.findAll().size();

        // Update the recordSense
        RecordSense updatedRecordSense = recordSenseRepository.findOne(recordSense.getId());
        // Disconnect from session so that the updates on updatedRecordSense are not directly saved in db
        em.detach(updatedRecordSense);
        updatedRecordSense
            .insert(UPDATED_INSERT)
            .path(UPDATED_PATH)
            .fileSentDate(UPDATED_FILE_SENT_DATE)
            .fileCreationDate(UPDATED_FILE_CREATION_DATE)
            .processStartDate(UPDATED_PROCESS_START_DATE)
            .processFinishDate(UPDATED_PROCESS_FINISH_DATE)
            .status(UPDATED_STATUS)
            .afid(UPDATED_AFID)
            .afidContentType(UPDATED_AFID_CONTENT_TYPE)
            .isProcessed(UPDATED_IS_PROCESSED)
            .similarity(UPDATED_SIMILARITY);

        restRecordSenseMockMvc.perform(put("/api/record-senses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecordSense)))
            .andExpect(status().isOk());

        // Validate the RecordSense in the database
        List<RecordSense> recordSenseList = recordSenseRepository.findAll();
        assertThat(recordSenseList).hasSize(databaseSizeBeforeUpdate);
        RecordSense testRecordSense = recordSenseList.get(recordSenseList.size() - 1);
        assertThat(testRecordSense.getInsert()).isEqualTo(UPDATED_INSERT);
        assertThat(testRecordSense.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testRecordSense.getFileSentDate()).isEqualTo(UPDATED_FILE_SENT_DATE);
        assertThat(testRecordSense.getFileCreationDate()).isEqualTo(UPDATED_FILE_CREATION_DATE);
        assertThat(testRecordSense.getProcessStartDate()).isEqualTo(UPDATED_PROCESS_START_DATE);
        assertThat(testRecordSense.getProcessFinishDate()).isEqualTo(UPDATED_PROCESS_FINISH_DATE);
        assertThat(testRecordSense.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRecordSense.getAfid()).isEqualTo(UPDATED_AFID);
        assertThat(testRecordSense.getAfidContentType()).isEqualTo(UPDATED_AFID_CONTENT_TYPE);
        assertThat(testRecordSense.isIsProcessed()).isEqualTo(UPDATED_IS_PROCESSED);
        assertThat(testRecordSense.getSimilarity()).isEqualTo(UPDATED_SIMILARITY);
    }

    @Test
    @Transactional
    public void updateNonExistingRecordSense() throws Exception {
        int databaseSizeBeforeUpdate = recordSenseRepository.findAll().size();

        // Create the RecordSense

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRecordSenseMockMvc.perform(put("/api/record-senses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recordSense)))
            .andExpect(status().isCreated());

        // Validate the RecordSense in the database
        List<RecordSense> recordSenseList = recordSenseRepository.findAll();
        assertThat(recordSenseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRecordSense() throws Exception {
        // Initialize the database
        recordSenseRepository.saveAndFlush(recordSense);
        int databaseSizeBeforeDelete = recordSenseRepository.findAll().size();

        // Get the recordSense
        restRecordSenseMockMvc.perform(delete("/api/record-senses/{id}", recordSense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RecordSense> recordSenseList = recordSenseRepository.findAll();
        assertThat(recordSenseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordSense.class);
        RecordSense recordSense1 = new RecordSense();
        recordSense1.setId(1L);
        RecordSense recordSense2 = new RecordSense();
        recordSense2.setId(recordSense1.getId());
        assertThat(recordSense1).isEqualTo(recordSense2);
        recordSense2.setId(2L);
        assertThat(recordSense1).isNotEqualTo(recordSense2);
        recordSense1.setId(null);
        assertThat(recordSense1).isNotEqualTo(recordSense2);
    }
    
    public void prepareData(String personName,String date,Device device,Person person1) {
    	    	
    	Image image1 = new Image();
    	image1.setPerson(person1);
    	image1.setImage("sdf".getBytes());
    	image1.setAfid("sdasd".getBytes());
    	image1.setImageContentType("");
    	image1.setAfidContentType("");
    	imageRepository.save(image1);
    	
    	RecordSense record1 = new RecordSense();
    	record1.setImage(image1);
    	LocalDateTime date1 = LocalDateTime.parse(date);
    	record1.setInsert(date1.toInstant(ZoneOffset.UTC));
    	record1.setDevice(device);
    	recordSenseRepository.save(record1);
    }
    
    
    @Test
    @Transactional
    public void getRecordForKnownPeople() throws Exception {
        // Initialize the database
    	
    	
    	Device device = new Device();
    	device.setDeviceId("1");
    	device.setDescription("1");
    	device.setDeviceType(DeviceType.INPUT);
    	
    	Device device2 = new Device();
    	device2.setDeviceId("2");
    	device2.setDescription("2");
    	device2.setDeviceType(DeviceType.OUTPUT);

    	deviceRepository.save(device);
    	deviceRepository.save(device2);
    	
    	Person person1 = new Person();
    	person1.setName("a");
    	personRepository.save(person1);
    	
    	Person person2 = new Person();
    	person2.setName("b");
    	personRepository.save(person2);
    	
    	Person person3 = new Person();
    	person3.setName("c");
    	personRepository.save(person3);
    	
    	
    	prepareData("a", "2020-09-01T08:11:30", device,person1);
    	prepareData("b", "2020-09-01T10:11:30", device,person1);
    	prepareData("c", "2020-09-01T17:11:30", device2,person1);
    	prepareData("d", "2020-09-01T09:11:30", device,person2);
    	prepareData("e", "2020-09-01T11:11:30", device,person2);
    	    	
    	recordSenseRepository.findAll();
    	String startDate = "2020-01-01T00:00:00.00";
    	String endDate = "2021-01-01T00:00:00.00";
    	// Get all the recordList
        MvcResult mvcResult = restRecordSenseMockMvc.perform(get("/api/record-senses/getRecordsForReportForKnownPerson-2?startDate="+startDate+"&endDate="+endDate))
            .andExpect(status().isOk()).andReturn();
    
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<RecordReportVM> asList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<RecordReportVM>>() { });
        assertThat(asList.size()).isEqualTo(2);
    
    
        MvcResult mvcResult2 = restRecordSenseMockMvc.perform(get("/api/record-senses/getRecordsForReportDidntCome?startDate="+startDate+"&endDate="+endDate))
                .andExpect(status().isOk()).andReturn();
        List<RecordReportVM> asList2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<List<RecordReportVM>>() { });
        assertThat(asList2.size()).isEqualTo(1);
        assertThat(asList2.get(0).getPersonName()).isEqualTo(person3.getName());
    }

    
}
