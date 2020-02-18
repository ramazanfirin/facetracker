package com.mastertek.service;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mastertek.domain.Image;
import com.mastertek.domain.Record;
import com.mastertek.domain.enumeration.PersonType;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.RecordRepository;

@Service
public class MatchingService {

	final private MatchingWhiteListService matchingWhiteListService;
	
	final private MatchingBlackListService matchingBlackListService;
	
	final private ImageRepository imageRepository;

	final private RecordRepository recordRepository;
	
	final private AyonixEngineService ayonixEngineService;
	
	public MatchingService(MatchingWhiteListService matchingWhiteListService,
			MatchingBlackListService matchingBlackListService,ImageRepository imageRepository,RecordRepository recordRepository,AyonixEngineService ayonixEngineService) {
		super();
		this.matchingWhiteListService = matchingWhiteListService;
		this.matchingBlackListService = matchingBlackListService;
		this.imageRepository = imageRepository;
		this.recordRepository = recordRepository;
		this.ayonixEngineService = ayonixEngineService;
	}

	public Record checkForMatching(Record record) {
		List<Image> images = imageRepository.findAll();
		
		float similarityRate = 0f;
		Image selectedImage = null;
		for (Iterator iterator = images.iterator(); iterator.hasNext();) {
			Image image = (Image) iterator.next();
			float similarityRateTemp = ayonixEngineService.match(record.getAfid(), image.getAfid());
			if(similarityRateTemp>similarityRate) {
				similarityRate = similarityRateTemp;
				selectedImage = image;
			}
		}
		
		if(similarityRate>0.9)
			process(record, selectedImage);
		else
			record.setStatus(RecordStatus.NO_MATCHING);
		
		record.setIsProcessed(true);
		record.setProcessFinishDate(Instant.now());
		recordRepository.save(record);
		
		return record;
	}
	
	private void process(Record record,Image image) {
		if(image.getPerson().getType()==PersonType.WHITE_LIST_PERSON)
			matchingWhiteListService.process(record,image);
	
		if(image.getPerson().getType()==PersonType.BLACK_LIST_PERSON)
			matchingBlackListService.process(record,image);
	
	}
	
	
}
