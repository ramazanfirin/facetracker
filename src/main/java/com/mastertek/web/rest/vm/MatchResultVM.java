package com.mastertek.web.rest.vm;

import com.mastertek.domain.Image;

public class MatchResultVM {
	float similarityRate = 0f;
	Image image = null;
	
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public float getSimilarityRate() {
		return similarityRate;
	}
	public void setSimilarityRate(float similarityRate) {
		this.similarityRate = similarityRate;
	}
}
