package com.mastertek.service;

import org.springframework.stereotype.Service;

import com.mastertek.domain.Image;
import com.mastertek.domain.Record;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.PersonRepository;
import com.mastertek.repository.RecordRepository;

@Service
public class MatchingBlackListService {

	
	final private RecordRepository recordRepository ;
	
	public MatchingBlackListService(RecordRepository recordRepository) {
		super();
		this.recordRepository = recordRepository;
	}
	
	public void process(Record record,Image image) {
		record.setImage(image);
		record.setStatus(RecordStatus.BLACK_LIST_DETECTED);
		recordRepository.save(record);
	}
	
}
