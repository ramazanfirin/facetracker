package com.mastertek.service;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.FacetrackerApp;
import com.mastertek.domain.Device;
import com.mastertek.domain.Image;
import com.mastertek.domain.Person;
import com.mastertek.domain.Record;
import com.mastertek.domain.enumeration.PersonType;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.PersonRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.service.dto.SearchOnIndexResultDTO;
import com.vdt.face_recognition.sdk.Recognizer.SearchResult;
import com.vdt.face_recognition.sdk.Template;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@Transactional
public class SenseTimeRecognitionServiceIntTest {

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RecordRepository recordRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private DiviEngineService ayonixEngineService;
    
    @Autowired
    private ImageRepository imageRepository;
    
    Person whiteListPerson;

    Person blackListPerson;
    
    @Autowired
    SenseTimeService senseTimeService;
    
    @Before
    public void setup() {
    	
    	Device device = new Device();
    	device.setDeviceId("733935");
     	deviceRepository.save(device);
    	
    	whiteListPerson = new Person();
    	whiteListPerson.setName("whiteListPerson");
    	whiteListPerson.setType(PersonType.WHITE_LIST_PERSON);
    	personRepository.save(whiteListPerson);
    	
    	blackListPerson = new Person();
    	blackListPerson.setName("blackListPerson");
    	blackListPerson.setType(PersonType.BLACK_LIST_PERSON);
    	personRepository.save(blackListPerson);
     	
    }	

    @Test
    public void login() throws Exception {
    	
    	String data = senseTimeService.login();
    	System.out.println(data);
    }

    @Test
    public void detectFace() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/vesikalik/ahmet.degirmen.jpg").getFile());

    	String data = senseTimeService.login();
    	Boolean hasFace = senseTimeService.hasFace(file.getAbsolutePath(), data);
    	assertThat(hasFace).isTrue();
    }
    
    @Test
    public void detectFace2() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797507.jpg").getFile());

    	String data = senseTimeService.login();
    	Boolean hasFace = senseTimeService.hasFace(file.getAbsolutePath(), data);
    	assertThat(hasFace).isFalse();
    }

    @Test
    public void search_Found() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797506.jpg").getFile());

    	String data = senseTimeService.login();
    	com.mastertek.web.rest.util.sensetime.SearchResult result = senseTimeService.search(file.getAbsolutePath(), data);
    	assertThat(result.getSimilarity()).isGreaterThan(80);
    }
    @Test
    public void search_Not_Found() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/vesikalik/ahmet.degirmen.jpg").getFile());

    	String data = senseTimeService.login();
    	com.mastertek.web.rest.util.sensetime.SearchResult result = senseTimeService.search(file.getAbsolutePath(), data);
    	assertThat(result.getSimilarity()).isEqualTo(0);
    }
    @Test
    public void search_Face_Not_Found() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797507.jpg").getFile());

    	String data = senseTimeService.login();
    	com.mastertek.web.rest.util.sensetime.SearchResult result = senseTimeService.search(file.getAbsolutePath(), data);
    	assertThat(result.getSimilarity()).isEqualTo(0);
    }
}
