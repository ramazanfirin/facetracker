package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.Device;
import com.mastertek.domain.Record;
import com.mastertek.domain.RecordSense;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.repository.RecordSenseRepository;
import com.mastertek.service.dto.FaceDataDTO;
import com.mastertek.service.util.FaceUtil;


@Service
@Transactional
public class FaceRecognitionSenseTimeService {

	private final Logger log = LoggerFactory.getLogger(FaceRecognitionSenseTimeService.class);
	final private ApplicationProperties applicationProperties;
	
	final private DiviEngineService ayonixEngineService;
	
	final private RecordSenseRepository recordSenseRepository;
	
	final private DeviceRepository deviceRepository;
	
	final private MatchingService matchingService;
	
	final private SenseTimeService senseTimeService;
	
	public FaceRecognitionSenseTimeService(ApplicationProperties applicationProperties,DiviEngineService ayonixEngineService,RecordSenseRepository recordSenseRepository,DeviceRepository deviceRepository,MatchingService matchingService,SenseTimeService senseTimeService) throws Exception {
		super();
		this.applicationProperties = applicationProperties;
		this.ayonixEngineService = ayonixEngineService;
		this.recordSenseRepository = recordSenseRepository;
		this.deviceRepository = deviceRepository;
		this.matchingService = matchingService;
		this.senseTimeService = senseTimeService;
	}
	
	
	
	public void analyze(String uuid,String fileName) throws Exception {
		analyze(uuid, fileName, 0l);
	}

	//@Async
	public void analyze(String uuid,String fileName,Long fileCatalogId) throws Exception {
		RecordSense recordSense = new RecordSense();
		try {
			log.info("uuid="+uuid+",filename:"+fileName+" analiz başlıyor");
			File file = new File(fileName);
			recordSense = startFileProcessing(fileName);
			
			String token = senseTimeService.login();
			Boolean hasFace = senseTimeService.hasFace(fileName, token);
			if(!hasFace) {
				NoFaceDetected(recordSense);
			}else {
				saveRecord(recordSense,token);
			}

			finishFileProcessing(recordSense);
			log.info("uuid="+uuid+",filename:"+fileName+" analiz bitti");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			recordSense.setPath(fileName);
			recordSense.setIsProcessed(false);
			recordSense.setStatus(RecordStatus.ERROR);
			recordSenseRepository.save(recordSense);
		}
	}
	
	private RecordSense startFileProcessing(String fileName) throws Exception {
		RecordSense record = new RecordSense();
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
		recordSenseRepository.save(record);
		
		return record;
	}

	private void finishFileProcessing(RecordSense record) {
		record.setProcessFinishDate(Instant.now());
		recordSenseRepository.save(record);
	}
	

	private void NoFaceDetected(RecordSense record) throws IOException {
		record.setStatus(RecordStatus.NO_FACE_DETECTED);
		record.setProcessFinishDate(Instant.now());
		recordSenseRepository.save(record);
	}
	
	private void NoAfidDetected(RecordSense record) throws IOException {
		record.setStatus(RecordStatus.NO_AFID_DETECTED);
		record.setProcessFinishDate(Instant.now());
		recordSenseRepository.save(record);
	}
	
	private void saveRecord(RecordSense record,String token) throws Exception {
		matchingService.checkForMatching(record,token);
		
	}
	
	
}