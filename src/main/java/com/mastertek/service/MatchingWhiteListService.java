package com.mastertek.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mastertek.domain.Image;
import com.mastertek.domain.Record;
import com.mastertek.domain.WhiteListRecordItem;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.ImageRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.repository.WhiteListPersonRepository;
import com.mastertek.repository.WhiteListRecordItemRepository;

@Service
public class MatchingWhiteListService {
	
	final private AyonixEngineService ayonixEngineService;
	final private ImageRepository imageRepository ;
	final private WhiteListRecordItemRepository whiteListRecordItemRepository ;
	final private RecordRepository recordRepository ;
	
	public MatchingWhiteListService(WhiteListPersonRepository whiteListPersonRepository,AyonixEngineService ayonixEngineService,ImageRepository imageRepository,WhiteListRecordItemRepository whiteListRecordItemRepository,RecordRepository recordRepository) {
		super();
		this.ayonixEngineService = ayonixEngineService;
		this.imageRepository = imageRepository;
		this.whiteListRecordItemRepository = whiteListRecordItemRepository;
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
		WhiteListRecordItem item = new WhiteListRecordItem();
		item.setPerson(image.getWhiteListPerson());
		item.setRecord(record);
		whiteListRecordItemRepository.save(item);
		
		record.setStatus(RecordStatus.WHITE_LIST_DETECTED);
		recordRepository.save(record);
	}

}
