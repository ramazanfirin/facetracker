package com.mastertek.service.dto;

import com.vdt.face_recognition.sdk.Template;

public class FaceDataDTO {

	private byte[] template;
	
	private Template diviTemplate;

	public byte[] getTemplate() {
		return template;
	}

	public void setTemplate(byte[] template) {
		this.template = template;
	}

	public Template getDiviTemplate() {
		return diviTemplate;
	}

	public void setDiviTemplate(Template diviTemplate) {
		this.diviTemplate = diviTemplate;
	}
	
	
}
