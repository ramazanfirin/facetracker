package com.mastertek.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mastertek.config.ApplicationProperties;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.service.dto.FaceDataDTO;
import com.vdt.face_recognition.sdk.Capturer;
import com.vdt.face_recognition.sdk.FacerecService;
import com.vdt.face_recognition.sdk.RawSample;
import com.vdt.face_recognition.sdk.Recognizer;
import com.vdt.face_recognition.sdk.Recognizer.MatchResult;
import com.vdt.face_recognition.sdk.Template;

@Service
public class DiviEngineService {

	private final Logger log = LoggerFactory.getLogger(DiviEngineService.class);
	//FaceID sdk;
	String writeDirectory;
	
	DeviceRepository deviceRepository;
	
	private ApplicationProperties applicationProperties;
	
	FacerecService service;
	
	Capturer capturer;
	
	Recognizer recognizer;
	
	public DiviEngineService(ApplicationProperties applicationProperties,DeviceRepository deviceRepository) {
		super();
		// TODO Auto-generated constructor stub
		this.deviceRepository = deviceRepository;
		this.applicationProperties = applicationProperties;
	}

	@PostConstruct
	public void init() {
		//if(!"tomcat".equals(applicationProperties.getEnvironment()))
		//	sdk = new FaceID("C:\\Program Files\\Ayonix Corporation\\Ayonix FaceID SDK v6\\data\\engine");
		
		service = FacerecService.createService(
				applicationProperties.getDllPath(),
				applicationProperties.getFaceRegPath(),
				applicationProperties.getLicensePath());
				
		
		FacerecService.Config capturer_conf = service.new Config(applicationProperties.getCapturer());
		capturer_conf.overrideParameter("downscale_rawsamples_to_preferred_size", 0);
		capturer= service.createCapturer(capturer_conf);
		
		recognizer = service.createRecognizer(applicationProperties.getRecognizer(), true, true, true);
		
		System.out.println(" 3 divi start bitti.");
		System.out.println(" capturer:"+applicationProperties.getCapturer());
		System.out.println(" recognizer:"+applicationProperties.getRecognizer());
	}
	
	
	
	
	public List<FaceDataDTO> analyze(String path) throws Exception  {
		
		List<FaceDataDTO> result = new ArrayList<FaceDataDTO>();
			
			Vector<RawSample> faces = detectFaces(path);
			for (int i = 0; i < faces.size(); i++) {
				FaceDataDTO recordTemp = getRecord(faces.get(i), path);
				//recordTemp.setAyonixFace(faces[i]);
				result.add(recordTemp);
			}
			
			return result;
			
		
	}
	
	
	
	public Vector<RawSample> detectFaces(String path) throws Exception {
		
		BufferedImage image= ImageIO.read(new File(path));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		 
		Vector<RawSample> samples=capturer.capture(out.toByteArray()); 
		 
		 return samples;
	}

	
	
	
	public FaceDataDTO getRecord(RawSample faceDetail,String fileName) throws IOException {
		ByteArrayOutputStream bOutput = new ByteArrayOutputStream(1200);;
		Template temp = null;
		
		try {
			temp = recognizer.processing(faceDetail);
			bOutput = new ByteArrayOutputStream(1200);
			temp.save(bOutput);
		} catch (Exception e) {
			e.printStackTrace();
			bOutput = null;
		}
       
		FaceDataDTO dataDTO = new FaceDataDTO();
		dataDTO.setTemplate(bOutput.toByteArray());
		dataDTO.setDiviTemplate(temp);
		
		return dataDTO;	
		
	}
	


	
	public float match(Template afid1, Template afid2) {
		MatchResult matchResult= recognizer.verifyMatch(afid1, afid2);
		Double result = matchResult.score;
		return result.floatValue();
	}
	
	public Template loadTemplate(byte[] array) {
		return recognizer.loadTemplate(new ByteArrayInputStream(array));
	}
	
	public Template getTemplate(byte[] array) {
		
		Vector<RawSample> samples=capturer.capture(array);
		if(samples.size()==0)
			throw new RuntimeException("Farklı bir resim ile deneyiniz");
		
		Template template = recognizer.processing(samples.get(0));
		return template;
	}
	
	public Template getTemplate(String path) throws Exception {
		BufferedImage image2= ImageIO.read(new File(path));
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		ImageIO.write(image2, "png", out2);
		
		Vector<RawSample> samples=capturer.capture(out2.toByteArray());
		Template template = recognizer.processing(samples.get(0));
		return template;
	}
}
