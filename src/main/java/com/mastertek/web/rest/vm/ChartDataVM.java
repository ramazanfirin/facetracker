package com.mastertek.web.rest.vm;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChartDataVM {

	List<String> labels = new ArrayList<String>();
	List<String> series = new ArrayList<String>();
	List<Long> datas = new ArrayList<Long>();
	List<Long> entryStandart = new ArrayList<Long>();
	
	List<Long> temp = new ArrayList<Long>();
	
	List<Instant> entryList = new ArrayList<Instant>();
	List<Instant> entryStandartInstant = new ArrayList<Instant>();
	
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	public List<String> getSeries() {
		return series;
	}
	public void setSeries(List<String> series) {
		this.series = series;
	}
	public List<Long> getDatas() {
		return datas;
	}
	public void setDatas(List<Long> datas) {
		this.datas = datas;
	}
	public List<Long> getEntryStandart() {
		return entryStandart;
	}
	public void setEntryStandart(List<Long> entryStandart) {
		this.entryStandart = entryStandart;
	}
	public List<Long> getTemp() {
		return temp;
	}
	public void setTemp(List<Long> temp) {
		this.temp = temp;
	}
	public List<Instant> getEntryList() {
		return entryList;
	}
	public void setEntryList(List<Instant> entryList) {
		this.entryList = entryList;
	}
	public List<Instant> getEntryStandartInstant() {
		return entryStandartInstant;
	}
	public void setEntryStandartInstant(List<Instant> entryStandartInstant) {
		this.entryStandartInstant = entryStandartInstant;
	}
}
