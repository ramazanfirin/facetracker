package com.mastertek.service;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

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
import com.vdt.face_recognition.sdk.Template;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@Transactional
public class DiviFaceRecognitionServiceIntTest2 {

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
    public void testFaceNotDetected() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797507.jpg").getFile());

    	faceRecognitionService.analyze(UUID.randomUUID().toString(),file.getAbsolutePath());
    	
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	assertThat(list.get(0).getStatus()).isEqualTo(RecordStatus.NO_FACE_DETECTED);
    	
    
    }

    
    @Test
    public void no_afid() throws Exception {
    	
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_42839_1566849042090.jpg").getFile());
    	
    	faceRecognitionService.analyze(UUID.randomUUID().toString(),file.getAbsolutePath());
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	assertThat(list.get(0).getStatus()).isEqualTo(RecordStatus.NO_MATCHING);
    	
    	
    }
    
        
    @Test
    public void whiteListPersonDetected() throws Exception {
    
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797506.jpg").getFile());
    	
    	Template template = ayonixEngineService.getTemplate(file.getAbsolutePath());
    	ByteArrayOutputStream bOutput = new ByteArrayOutputStream(1200);
    	template.save(bOutput);
    	
    	Image image = new Image();
    	image.setAfid(bOutput.toByteArray());
    	image.setAfidContentType("afidContentType");
    	image.setPerson(whiteListPerson);
    	image.setImage("image".getBytes());
    	image.setImageContentType("dsf");
    	imageRepository.save(image);
    	
    	faceRecognitionService.analyze(UUID.randomUUID().toString(),file.getAbsolutePath());
    	
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	
    	Record record = list.get(0);
    	assertThat(record.getStatus()).isEqualTo(RecordStatus.WHITE_LIST_DETECTED);
    	
    	assertThat(record.getFileSentDate()).isEqualTo(Instant.ofEpochMilli(1557049797506l));
    	//assertThat(record.getFileCreationDate()).isNotNull();
    	assertThat(Instant.now().until(record.getProcessStartDate(),ChronoUnit.SECONDS)).isLessThan(1);
    	assertThat(Instant.now().until(record.getProcessFinishDate(),ChronoUnit.SECONDS)).isLessThan(1);
    	
    
    	
    }
    
    
    @Test
    public void blackListPersonDetected() throws Exception {
    	
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797506.jpg").getFile());
    	
    	Template template = ayonixEngineService.getTemplate(file.getAbsolutePath());
    	ByteArrayOutputStream bOutput = new ByteArrayOutputStream(1200);
    	template.save(bOutput);
    	
    	Image image = new Image();
    	image.setAfid(bOutput.toByteArray());
    	image.setAfidContentType("afidContentType");
    	image.setPerson(blackListPerson);
    	image.setImage("image".getBytes());
    	image.setImageContentType("dsf");
    	imageRepository.save(image);
    	
    	faceRecognitionService.analyze(UUID.randomUUID().toString(),file.getAbsolutePath());
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	
    	Record record = list.get(0);
    	assertThat(record.getStatus()).isEqualTo(RecordStatus.BLACK_LIST_DETECTED);
    	
    }
    
    @Test
    public void noMatching() throws Exception {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_19121_1557049797506.jpg").getFile());
    	
    	faceRecognitionService.analyze(UUID.randomUUID().toString(),file.getAbsolutePath());
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	
    	Record record = list.get(0);
    	assertThat(record.getStatus()).isEqualTo(RecordStatus.NO_MATCHING);
    	
    } 
}
