package com.mastertek.service;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mastertek.config.ApplicationProperties;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.service.dto.FaceDataDTO;
import com.mastertek.service.util.FaceUtil;

import ayonix.AyonixFace;
import ayonix.AyonixImage;
import ayonix.FaceID;

@Service
public class AyonixEngineService{

	private final Logger log = LoggerFactory.getLogger(AyonixEngineService.class);
	
	FaceID sdk;
	
	DeviceRepository deviceRepository;
	
	private ApplicationProperties applicationProperties;
	
	public AyonixEngineService(ApplicationProperties applicationProperties,DeviceRepository deviceRepository) {
		super();
		// TODO Auto-generated constructor stub
		this.deviceRepository = deviceRepository;
		this.applicationProperties = applicationProperties;
	}

	@PostConstruct
	public void init() {
		//if(!"tomcat".equals(applicationProperties.getEnvironment()))
		//	sdk = new FaceID("C:\\Program Files\\Ayonix Corporation\\Ayonix FaceID SDK v6\\data\\engine");
		
		//System.out.println("bitti");
	}
	
	@PreDestroy
	public void destroy() {
		sdk.Close();
	}
	
	public byte[] createTemplate(AyonixFace face) {
		return sdk.CreateAfid(face);
	}
	
	public void preProcess(AyonixFace face) {
		sdk.PreprocessFace(face);
	}
	
	public List<FaceDataDTO> analyze(String path) throws Exception  {
		
			List<FaceDataDTO> result = new ArrayList<FaceDataDTO>();
			
			AyonixFace[] faces = detectFaces(path);
			for (int i = 0; i < faces.length; i++) {
				FaceDataDTO faceDataDTO = getRecord(faces[i], path);
				result.add(faceDataDTO);
			}
			
			return result;
			
		
	}
	
	public byte[] getAfid(byte[] bytes) {
		AyonixImage img = sdk.LoadImage(bytes);
		AyonixFace[] faces = sdk.DetectFaces(img);
		if(faces.length==0)
			throw new RuntimeException("No face detected");
		if(faces.length>1)
			throw new RuntimeException("more face than 1");
		
		
		sdk.PreprocessFace(faces[0]);
		byte[] afid = sdk.CreateAfid(faces[0]);
		if(afid == null)
			throw new RuntimeException("afid not detected");
		
		return afid;
		
	}
	
	public byte[] getAfid(String path) {
		AyonixImage img = sdk.LoadImage(path);
		AyonixFace[] faces = sdk.DetectFaces(img);
		if(faces.length==0)
			throw new RuntimeException("No face detected");
		if(faces.length>1)
			throw new RuntimeException("more face than 1");
		
		
		sdk.PreprocessFace(faces[0]);
		byte[] afid = sdk.CreateAfid(faces[0]);
		if(afid == null)
			throw new RuntimeException("afid not detected");
		
		return afid;
		
	}
	
	public AyonixFace[] detectFaces(String path) throws Exception {
		
		 AyonixImage img =null;
		 if(FaceUtil.isUrl(path))
			 img= sdk.LoadImage(FaceUtil.getBytesOfImage(path));
		 else
			 img = sdk.LoadImage(path);
		 
		 AyonixFace[] faces = sdk.DetectFaces(img,48,new Rectangle(img.getWidth(), img.getHeight()));
		 if(faces.length==0) {
			 //faces = detectFaceFromResizedImage(path);
		 }else {
			 sdk.PreprocessFace(faces[0]);	
//			 if(faces[0].getQuality()<0.9)
//				 faces = detectFaceFromResizedImage(path);

		 }
		 
		 return faces;
	}


	public FaceDataDTO getRecord(AyonixFace faceDetail,String fileName) throws IOException {
	
		sdk.PreprocessFace(faceDetail);
		
        byte[] template = null;
		try {
			template = sdk.CreateAfid(faceDetail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
       
		FaceDataDTO dataDTO = new FaceDataDTO();
		dataDTO.setTemplate(template);
		
		return dataDTO;
	}
	
	public float match(byte[] afid1,byte[] afid2) {
		
		try {
			float[] scores = new float[1];
			
			int[] indexes = new int[1];
			
			Vector<byte[]> afids = new Vector<byte[]>();
			afids.add(afid2);
			
			sdk.MatchAfids(afid1, afids, scores, indexes);
			return scores[0];
		} catch (Exception e) {
			throw new RuntimeException(e);
			//return 0;
		}
		
	}
	
}
