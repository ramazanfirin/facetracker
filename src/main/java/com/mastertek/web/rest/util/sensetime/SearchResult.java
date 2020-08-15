package com.mastertek.web.rest.util.sensetime;

public class SearchResult {
	
	Long personId;
	
	Double similarity =0d;
	
	

	public SearchResult(Long personId, Double similarity) {
		super();
		this.personId = personId;
		this.similarity = similarity;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

}
