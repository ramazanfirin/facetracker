package com.mastertek.web.rest.vm;

import java.time.Instant;

public class RecordReportVM {

	
	Instant startDate;
	Instant endDate;
	String personName;
	Long personId;
	String type;
	
	String entryStatus;
	String entryDifferences;
	
	String exitStatus;
	String exitDifferences;
	
	String totalDuration;
	
	
	public Instant getStartDate() {
		return startDate;
	}
	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}
	public Instant getEndDate() {
		return endDate;
	}
	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEntryStatus() {
		return entryStatus;
	}
	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}
	public String getEntryDifferences() {
		return entryDifferences;
	}
	public void setEntryDifferences(String entryDifferences) {
		this.entryDifferences = entryDifferences;
	}
	public String getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(String exitStatus) {
		this.exitStatus = exitStatus;
	}
	public String getExitDifferences() {
		return exitDifferences;
	}
	public void setExitDifferences(String exitDifferences) {
		this.exitDifferences = exitDifferences;
	}
	public String getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}
	
	
	
}
