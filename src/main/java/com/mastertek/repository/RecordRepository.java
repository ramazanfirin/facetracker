package com.mastertek.repository;

import com.mastertek.domain.Record;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
	
	@Query( "SELECT record FROM Record record where record.image.person.id=:personId and record.insert>=:startDate and record.insert<=:endDate  ORDER BY record.insert desc")
	List<Record> findRecords(@Param("personId") Long personId,@Param("startDate") Instant startDate,@Param("endDate") Instant endDate);

}
