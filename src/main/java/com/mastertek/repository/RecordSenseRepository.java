package com.mastertek.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mastertek.domain.Person;
import com.mastertek.domain.RecordSense;


/**
 * Spring Data JPA repository for the RecordSense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordSenseRepository extends JpaRepository<RecordSense, Long> {

	@Query( "SELECT record FROM RecordSense record where record.image.person.id=:personId and record.insert>=:startDate and record.insert<=:endDate  ORDER BY record.insert desc")
	List<RecordSense> findRecords(@Param("personId") Long personId,@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	@Query( "SELECT record FROM RecordSense record where record.image is not null and record.insert>=:startDate and record.insert<=:endDate  ORDER BY record.insert desc")
	List<RecordSense> findRecordsForKnownPersons(@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	@Query( "SELECT record.image.person.id,record.image.person.name,record.device.deviceType,min(record.insert) FROM RecordSense record "
			+ "where record.device.deviceType='INPUT' and record.image is not null and record.insert>=:startDate and record.insert<=:endDate "
			+ "group by record.image.person.id,record.image.person.id,record.device.deviceType")
	List findRecordsForKnownPersonsForInput(@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	@Query( "SELECT record.image.person.id,record.image.person.name,record.device.deviceType,max(record.insert) FROM RecordSense record "
			+ "where record.device.deviceType='OUTPUT' and record.image is not null and record.insert>=:startDate and record.insert<=:endDate group by record.image.person.id,record.image.person.id,record.device.deviceType")
	List findRecordsForKnownPersonsForOutput(@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	
	@Query( "SELECT record FROM RecordSense record where record.image is null and record.insert>=:startDate and record.insert<=:endDate  ORDER BY record.insert desc")
	List<RecordSense> findRecordsForUnknownPersons(@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	
	@Query( "select person From Person person where person.id not in (SELECT record.image.person.id FROM RecordSense record where record.image is not null and record.insert>=:startDate and record.insert<=:endDate)")
	List<Person> findRecordsDidntCome(@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	@Query( "SELECT day(record.insert) ,month(record.insert),year(record.insert),min(record.insert) FROM RecordSense record "
			+ " where record.image.person.id=:personId and "
			+ " record.image is not null and "
			+ " record.insert>=:startDate and "
			+ " record.insert<=:endDate "
			+ " group by day(record.insert),month(record.insert),year(record.insert)")
	List findRecordsEntryDate(@Param("personId") Long personId,@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

	
}
