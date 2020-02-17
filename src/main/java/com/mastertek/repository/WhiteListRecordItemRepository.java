package com.mastertek.repository;

import com.mastertek.domain.WhiteListRecordItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WhiteListRecordItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhiteListRecordItemRepository extends JpaRepository<WhiteListRecordItem, Long> {

}
