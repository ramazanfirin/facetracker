package com.mastertek.repository;

import com.mastertek.domain.Record;
import com.mastertek.domain.RecordSense;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


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

	@Query( "SELECT record FROM RecordSense record where record.image is null and record.insert>=:startDate and record.insert<=:endDate  ORDER BY record.insert desc")
	List<RecordSense> findRecordsForUnknownPersons(@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

}
