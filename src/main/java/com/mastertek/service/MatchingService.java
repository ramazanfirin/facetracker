package com.mastertek.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.mastertek.domain.Image;
import com.mastertek.domain.Record;
import com.mastertek.domain.RecordSense;
import com.mastertek.domain.enumeration.PersonType;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.repository.RecordSenseRepository;
import com.mastertek.web.rest.util.sensetime.SearchResult;
import com.mastertek.web.rest.vm.MatchResultVM;
import com.vdt.face_recognition.sdk.Template;

@Service
public class MatchingService {

	final private MatchingWhiteListService matchingWhiteListService;
	
	final private MatchingBlackListService matchingBlackListService;
	
	final private ImageRepository imageRepository;

	final private RecordRepository recordRepository;
	
	final private RecordSenseRepository recordSenseRepository;
	
	final private DiviEngineService ayonixEngineService;
	
	final private SenseTimeService senseTimeService;
	
	public MatchingService(MatchingWhiteListService matchingWhiteListService,
			MatchingBlackListService matchingBlackListService,ImageRepository imageRepository,RecordRepository recordRepository,
			DiviEngineService ayonixEngineService,RecordSenseRepository recordSenseRepository,SenseTimeService senseTimeService) {
		super();
		this.matchingWhiteListService = matchingWhiteListService;
		this.matchingBlackListService = matchingBlackListService;
		this.imageRepository = imageRepository;
		this.recordRepository = recordRepository;
		this.ayonixEngineService = ayonixEngineService;
		this.recordSenseRepository = recordSenseRepository;
		this.senseTimeService = senseTimeService;
	}

	public Record checkForMatching(Record record) {
		List<Image> images = imageRepository.findAll();
		
		float similarityRate = 0f;
		Image selectedImage = null;
		for (Iterator iterator = images.iterator(); iterator.hasNext();) {
			Image image = (Image) iterator.next();
			Template temp = ayonixEngineService.loadTemplate(image.getAfid());
			float similarityRateTemp = ayonixEngineService.match(record.getDiviTemplate(), temp);
			if(similarityRateTemp>similarityRate) {
				similarityRate = similarityRateTemp;
				selectedImage = image;
			}
		}
		
		record.setSimilarity(similarityRate);
		if(similarityRate>0.85) {
			process(record, selectedImage);
		}else {
			record.setStatus(RecordStatus.NO_MATCHING);
		}
		
		record.setIsProcessed(true);
		record.setProcessFinishDate(Instant.now());
		recordRepository.save(record);
		
		return record;
	}
	
	public RecordSense checkForMatching(RecordSense record,String sessionId) throws Exception {
		SearchResult ad= senseTimeService.search(record.getPath(), sessionId);
		
		record.setSimilarity(ad.getSimilarity().floatValue());
		if(ad.getSimilarity()>0.85) {
			Image image = imageRepository.findOne(ad.getPersonId());
			record.setImage(image);
			record.setStatus(RecordStatus.WHITE_LIST_DETECTED);
			recordSenseRepository.save(record);
		}else {
			record.setStatus(RecordStatus.NO_MATCHING);
		}
		
		record.setIsProcessed(true);
		record.setProcessFinishDate(Instant.now());
		recordSenseRepository.save(record);
		
		return record;
	}
	
	public MatchResultVM checkForMatching(byte[] sourceImage) {
		MatchResultVM result = new MatchResultVM();
		//byte[] afid =ayonixEngineService.getAfid(sourceImage);
		Template afid =ayonixEngineService.loadTemplate(sourceImage);
		
		List<Image> images = imageRepository.findAll();
		
		float similarityRate = 0f;
		Image selectedImage = null;
		for (Iterator iterator = images.iterator(); iterator.hasNext();) {
			Image image = (Image) iterator.next();
			Template temp = ayonixEngineService.loadTemplate(image.getAfid()); 
			float similarityRateTemp = ayonixEngineService.match(afid, temp);
			if(similarityRateTemp>similarityRate) {
				similarityRate = similarityRateTemp;
				selectedImage = image;
			}
		}
		
		if(similarityRate>0.9) {
			result.setSimilarityRate(similarityRate);
			result.setImage(selectedImage);
			
		}
		
		return result;
	}
	
	private void process(Record record,Image image) {
		if(image.getPerson().getType()==PersonType.WHITE_LIST_PERSON)
			matchingWhiteListService.process(record,image);
	
		if(image.getPerson().getType()==PersonType.BLACK_LIST_PERSON)
			matchingBlackListService.process(record,image);
	
	}
	
	
}
