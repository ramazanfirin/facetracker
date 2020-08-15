package com.mastertek.repository;

import com.mastertek.domain.RecordSense;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RecordSense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordSenseRepository extends JpaRepository<RecordSense, Long> {

}
