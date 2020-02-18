package com.mastertek.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.Device;
import com.mastertek.domain.Record;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.service.dto.FaceDataDTO;
import com.mastertek.service.util.FaceUtil;


@Service
@Transactional
public class FaceRecognitionService {

	private final Logger log = LoggerFactory.getLogger(FaceRecognitionService.class);
	final private ApplicationProperties applicationProperties;
	
	final private AyonixEngineService ayonixEngineService;
	
	final private RecordRepository recordRepository;
	
	final private DeviceRepository deviceRepository;
	
	final private MatchingService matchingService;
	
	public FaceRecognitionService(ApplicationProperties applicationProperties,AyonixEngineService ayonixEngineService,RecordRepository recordRepository,DeviceRepository deviceRepository,MatchingService matchingService) throws Exception {
		super();
		this.applicationProperties = applicationProperties;
		this.ayonixEngineService = ayonixEngineService;
		this.recordRepository = recordRepository;
		this.deviceRepository = deviceRepository;
		this.matchingService = matchingService;
	}
	
	
	
	public void analyze(String uuid,String fileName) throws Exception {
		analyze(uuid, fileName, 0l);
	}

	//@Async
	public void analyze(String uuid,String fileName,Long fileCatalogId) throws Exception {
		log.info("uuid="+uuid+",filename:"+fileName+" analiz başlıyor");
		File file = new File(fileName);
		Record record = startFileProcessing(fileName);
		
		List<FaceDataDTO> faceDataDTOList = ayonixEngineService.analyze(fileName);
		if(faceDataDTOList.size() == 0) {
			NoFaceDetected(record);
		}else {
			FaceDataDTO faceDataDTO = faceDataDTOList.get(0);
			record.setAfid(faceDataDTO.getTemplate());
			saveRecord(record);
		}

		finishFileProcessing(record);
		log.info("uuid="+uuid+",filename:"+fileName+" analiz bitti");
	}
	
	private Record startFileProcessing(String fileName) throws Exception {
		Record record = new Record();
		//record.setFileCreationDate(Files.readAttributes(fileName, BasicFileAttributes.class).creationTime().toInstant());
		record.setFileSentDate(FaceUtil.getCreateDate(fileName));
		record.setProcessStartDate(Instant.now());
		record.setInsert(Instant.now());
		
		String source = FaceUtil.getSource(fileName);
		Device device = deviceRepository.findOneByDeviceId(source);
	    if(device == null) {
	    	if(!source.equals("733935") && !source.equals("744272"))
	    		throw new RuntimeException("device data can not be null.source:"+source);
			
		}
	    
	    record.setDevice(device);
		record.setPath(fileName);
		record.setIsProcessed(false);
		recordRepository.save(record);
		
		return record;
	}

	private void finishFileProcessing(Record record) {
		record.setProcessFinishDate(Instant.now());
		recordRepository.save(record);
	}
	

	private void NoFaceDetected(Record record) throws IOException {
		record.setStatus(RecordStatus.NO_FACE_DETECTED);
		record.setProcessFinishDate(Instant.now());
		recordRepository.save(record);
	}
	
	private void NoAfidDetected(Record record) throws IOException {
		record.setStatus(RecordStatus.NO_AFID_DETECTED);
		record.setProcessFinishDate(Instant.now());
		recordRepository.save(record);
	}
	
	private void saveRecord(Record record) throws Exception {
		
		if(record.getAfid()==null) {
			NoAfidDetected(record);
			return;
		}	
		
		matchingService.checkForMatching(record);
		
	}
	
	
}