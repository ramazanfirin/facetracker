package com.mastertek.web.rest.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import com.mastertek.web.rest.vm.RecordReportVM;

public class FaceTrackerUtil {

	public static void calculateStartDate(RecordReportVM recordReportVM) {
		if(recordReportVM.getStartDate()==null)
			return;
		
		Instant temp = recordReportVM.getStartDate();

		temp = recordReportVM.getStartDate().atZone(ZoneOffset.of("+03:00")).withHour(8).withMinute(30).toInstant();
		
		
		Long diffirences = ChronoUnit.MINUTES.between(temp, recordReportVM.getStartDate());
		
		if(diffirences<0) {
			recordReportVM.setEntryStatus("Normal Giriş");
			//recordReportVM.setEntryDifferences(diffirences.toString()+" dakika");
		}else {
			recordReportVM.setEntryStatus("Geç Giriş");
			recordReportVM.setEntryDifferences(diffirences.toString()+" dakika");
	
		}
	}
	
	public static void calculateEndDate(RecordReportVM recordReportVM) {
		if(recordReportVM.getEndDate()==null)
			return;
		Instant temp = recordReportVM.getEndDate();
		
		temp = recordReportVM.getStartDate().atZone(ZoneOffset.of("+03:00")).withHour(17).withMinute(30).toInstant();
		Long diffirences = ChronoUnit.MINUTES.between(temp, recordReportVM.getEndDate());
		
		if(diffirences>0) {
			recordReportVM.setExitStatus("Geç Çıkış");
			recordReportVM.setExitDifferences(diffirences+" dakika");
		}else {
			recordReportVM.setExitStatus("Erken Çıkış");
			recordReportVM.setExitDifferences(diffirences+" dakika");
		}
	}
	
	public static void calculateTotalDuration(RecordReportVM recordReportVM) {
		if(recordReportVM.getStartDate()==null || recordReportVM.getEndDate()==null)
			return;
		if(recordReportVM.getStartDate().isAfter(recordReportVM.getEndDate()))
			return;
			
		Long diffirences = ChronoUnit.MINUTES.between(recordReportVM.getStartDate(), recordReportVM.getEndDate());
		
		int hours = diffirences.intValue() / 60; //since both are ints, you get an int
		int minutes = diffirences.intValue() % 60;
		
		String result ="";
		if(hours>0)
			result = result + hours+ " saat";
		
		result = result + " "+ minutes+ " dakika";
		
		recordReportVM.setTotalDuration(result);
	}
	
}
