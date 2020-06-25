package com.mastertek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastertek.domain.Record;
import com.mastertek.service.dto.FaceDataDTO;
import com.vdt.face_recognition.sdk.Template;



@Service
public class FaceEngineService {
	
	@Autowired
	AyonixEngineService ayonixEngineService;
	
	@Autowired
	DiviEngineService diviEngineService;
	
	public List<FaceDataDTO> analyze(String path) throws Exception{
		return diviEngineService.analyze(path);
	}
	
	public float match(Template afid1,Template afid2) {
		return diviEngineService.match(afid1, afid2);	}
}
