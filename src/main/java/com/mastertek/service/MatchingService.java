package com.mastertek.service;

import org.springframework.stereotype.Service;

import com.mastertek.domain.Record;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.RecordRepository;

@Service
public class MatchingService {

	final private MatchingWhiteListService matchingWhıteListService;
	
	final private MatchingBlackListService matchingBlackListService;
	
	final private ImageRepository imageRepository;

	final private RecordRepository recordRepository;
	
	public MatchingService(MatchingWhiteListService matchingWhıteListService,
			MatchingBlackListService matchingBlackListService,ImageRepository imageRepository,RecordRepository recordRepository) {
		super();
		this.matchingWhıteListService = matchingWhıteListService;
		this.matchingBlackListService = matchingBlackListService;
		this.imageRepository = imageRepository;
		this.recordRepository = recordRepository;
	}

	public Record checkForMatching(Record record) {
		checkForWhiteList(record);
		checkForBlackList(record);
		
		if(record.getStatus().toString().equals(RecordStatus.PROCESSING_STARTED.toString())) {
			record.setStatus(RecordStatus.PROCESSING_FINISHED);
			recordRepository.save(record);
		}
		return record;
	}
	
	private void checkForWhiteList(Record record) {
		matchingWhıteListService.check(record);
	}
	
	private void checkForBlackList(Record record) {
		matchingBlackListService.check(record);
	}
}
