package com.mastertek.service;



import java.util.List;

import com.mastertek.domain.Record;

public interface FaceRecognitionInterface {

	public List<Record> analyze(String path) throws Exception;
	
	public float match(Object afid1,Object afid2);
	

}
