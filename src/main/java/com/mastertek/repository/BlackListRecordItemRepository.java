package com.mastertek.repository;

import com.mastertek.domain.BlackListRecordItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BlackListRecordItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlackListRecordItemRepository extends JpaRepository<BlackListRecordItem, Long> {

}
