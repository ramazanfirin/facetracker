package com.mastertek.repository;

import com.mastertek.domain.WhiteListPerson;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WhiteListPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhiteListPersonRepository extends JpaRepository<WhiteListPerson, Long> {

}
