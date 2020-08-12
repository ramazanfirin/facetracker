package com.mastertek.service.dto;

public class SearchOnIndexResultDTO {
	
	Long index;
	
	Double score = 0d;
	
	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	
	
	
}
