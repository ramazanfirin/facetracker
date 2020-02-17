package com.mastertek.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mastertek.domain.BlackListRecordItem;
import com.mastertek.domain.Image;
import com.mastertek.domain.Record;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.BlackListRecordItemRepository;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.repository.WhiteListRecordItemRepository;

@Service
public class MatchingBlackListService {

	final private AyonixEngineService ayonixEngineService;
	final private ImageRepository imageRepository ;
	final private BlackListRecordItemRepository blackListRecordItemRepository ;
	final private RecordRepository recordRepository ;
	
	public MatchingBlackListService(AyonixEngineService ayonixEngineService, ImageRepository imageRepository,
			BlackListRecordItemRepository blackListRecordItemRepository, RecordRepository recordRepository) {
		super();
		this.ayonixEngineService = ayonixEngineService;
		this.imageRepository = imageRepository;
		this.blackListRecordItemRepository = blackListRecordItemRepository;
		this.recordRepository = recordRepository;
	}
	
	public void check(Record record) {
		List<Image> images = imageRepository.findAll();
		
		for (Iterator iterator = images.iterator(); iterator.hasNext();) {
			Image image = (Image) iterator.next();
			float similarityRateTemp = ayonixEngineService.match(record.getAfid(), image.getAfid());
			if(similarityRateTemp>0.9) {
				create(record, image);
			}
		}
		
		
	}
	
	public void create(Record record,Image image) {
		BlackListRecordItem item = new BlackListRecordItem();
		item.setPerson(image.getBlackListPerson());
		item.setRecord(record);
		blackListRecordItemRepository.save(item);
		
		record.setStatus(RecordStatus.BLACK_LIST_DETECTED);
		recordRepository.save(record);
	}
	
}
